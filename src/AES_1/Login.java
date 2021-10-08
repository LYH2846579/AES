package AES_1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//引入键值对
import javafx.util.Pair;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

/**
 * @author LYHstart
 * @create 2021-10-08 16:13
 *
 * 实现口令的安全：
 *
 * 登陆计算机（或连接服务器）时，用户的口令作为分组密码密钥，加密某个固定的明文，生成的密文存储在计算机中。
 * 下次登陆时，把生成的密文和已存储的密文进行比较，若一致则登陆成功。也可用其它方法，只要实现口令登陆即可。
 *
 * 分析：
 * 这里采取的思路是分为注册与登录两个方式，并将所有的数据存储在MySQL数据库中
 * 由于要求用户输入的秘钥长度不定，这里采取md5算法，使用Hash函数处理,接下来将md5得到的一串128位的数据使用AES进行加密
 * 并将加密完毕的数据存储到数据库中 -> 这里可以使用随机数产生一些盐值salt来增强安全性
 *
 */
public class Login
{

    @Test
    public void test()
    {
        //register();
        login();
    }

    public static void register()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String user = scan.next();
        System.out.println("请输入密码:");
        String content = scan.next();
        //产生一个随机数,作为salt
        String salt = String.valueOf((int)(Math.random()*content.length()));
        System.out.println(salt);
        Pair<String, String> pair = md5(content, salt);
        String password = pair.getKey();

        //AES加密
        String[] strings = Login.nameKey();
        //将十六进制字符串转换为byte[]数组
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            key[i] = (byte) ((Character.digit(strings[i].charAt(0),16) << 4) + Character.digit(strings[i].charAt(1),16));
        }
        //将密码Hash进行AES加密
        //1.将由Hash得到的String转换为byte[]
        String[] strings1 = new String[16];
        String regStr = "\\w\\w";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(password);
        int j = 0;
        while(matcher.find())
        {
            String group = matcher.group(0);
            strings1[j++] = group;
        }
        //接下来将String数组转换为byte[]
        byte[] code = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            code[i] = (byte) ((Character.digit(strings1[i].charAt(0),16) << 4) + Character.digit(strings1[i].charAt(1),16));
        }

        word[] plaintext = Login.toWordArr(code);
        //秘钥处理
        word[] CipherKey = toWordArr(key);
        word[] cipherText = AES.encrypt(plaintext, CipherKey);
        password = wordArrStr(cipherText);

        //写入数据库
        Connection connection = JDBCUtils.getConnection();
        QueryRunner runner = new QueryRunner();
        String sql = "INSERT INTO users VALUES(?,?,?);";
        try
        {
            runner.update(connection,sql,user,salt,password);
        } catch (SQLException e)
        {
            //e.printStackTrace();
            //throw new RuntimeException("注册失败!");
            System.out.println("注册失败,用户名重复!");
        }finally
        {
            DbUtils.closeQuietly(connection);
        }
    }

    //验证登录
    public static void login()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String user = scan.next();
        System.out.println("请输入密码:");
        String content = scan.next();

        //常量
        String salt = null;
        String password = null;
        //1.查询用户名
        Connection connection = JDBCUtils.getConnection();
        String sql = "SELECT * FROM USERS WHERE user = ?;";
        QueryRunner runner = new QueryRunner();
        //实例化结果集
        ResultSetHandler handler = new BeanHandler(AESBean.class);
        Object query = null;
        try
        {
            query = runner.query(connection, sql, handler, user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        if(query instanceof AESBean)
        {
            AESBean e = (AESBean) query;
            salt = e.getSalt();
            password = e.getPassword();
        }
        else
        {
            System.out.println("类型错误!");
            return;
        }

        //校验是否相等 -> 首先计算md5
        //content += salt;
        Pair<String, String> pair = md5(content,salt);
        String input = pair.getKey();
        //将读取到的经过AES加密的密文进行解密
        //此时AES密文获取到的存储在password中,首先将password转换为byte[]数组进行解密
        //接下来将解密得到的明文(即为content+salt计算得到的Hash值)与输入的密码与salt得到的Hash值进行比对
        //1.类型转换
        //存储数组
        String[] strings1 = new String[16];
        byte[] bytes = new byte[16];
        //拆分
        String regStr = "\\w\\w";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(password);
        int j = 0;
        while(matcher.find())
        {
            String group = matcher.group(0);
            strings1[j++] = group;
        }
        //String[] -> byte[]
        for (int i = 0; i < 16; i++)
        {
            bytes[i] = (byte) ((Character.digit(strings1[i].charAt(0),16) << 4) + Character.digit(strings1[i].charAt(1),16));
        }
        //2.解密
        //首先将byte[]转换为word[]
        word[] cipherText = toWordArr(bytes);
        //解密
        //AES秘钥生成
        //获取使用姓名加密的秘钥
        String[] strings = nameKey();
        //将十六进制字符串转换为byte[]数组
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            key[i] = (byte) ((Character.digit(strings[i].charAt(0),16) << 4) + Character.digit(strings[i].charAt(1),16));
        }

        //获取秘钥word[]
        word[] CipherKey = toWordArr(key);
        //解密
        word[] newPlainText = AES.decrypt(cipherText, CipherKey);
        //将word[]转换为String
        String s = wordArrStr(newPlainText);


        if(input.equals(s))
        {
            System.out.println("登录成功!");
        }
        else
        {
            System.out.println("登录失败!");
        }

    }

    //md5加密算法
    public static Pair<String,String> md5(String data, String salt) {
        StringBuilder sb = new StringBuilder();
        try {
            data += salt;
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));

            // 将字节数据转换为十六进制
            for (byte b : md5) {
                sb.append(Integer.toHexString(b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String s = sb.toString();
        return new Pair<>(s,salt);
    }



    public static word[] toWordArr(byte[] b) {
        int len = b.length / 4;
        if (b.length % 4 != 0) len++;
        word[] w = new word[len];
        for (int i = 0; i < len; i++) {
            byte[] c = new byte[4];
            if (i * 4 < b.length) {
                for (int j = 0; j < 4; j++)
                    c[j] = b[i * 4 + j];
            }
            w[i] = new word(c);
        }
        return w;
    }

    public static String wordArrStr(word[] w) {
        String str = "";
        for (AES_1.word word : w)
            str += word;
        return str;
    }

    public static String[] nameKey()
    {
        String s = "网安刘亚东";
        byte[] bytes = s.getBytes();
        String name = "";
        String substr = "";
        for(byte b:bytes)
        {
            //输出一下截取之后的byte
            //System.out.println(b);
            substr = Integer.toHexString(b).substring(6,8) + " ";
            name += substr;
        }
        //补齐两位数
        name += "17";
        //拆分
        String[] split = name.split(" ");
        //System.out.println("******************");
        return split;
    }
}
