package AES_1;

import java.io.*;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LYHstart
 * @create 2021-10-9 19:34
 */
public class Test {

    public static void main(String[] args){
        //明文测试组
        byte[] plain = {
                (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x01,
                (byte) 0x01, (byte) 0xa1, (byte) 0x98, (byte) 0xaf,
                (byte) 0xda, (byte) 0x78, (byte) 0x17, (byte) 0x34,
                (byte) 0x86, (byte) 0x15, (byte) 0x35, (byte) 0x66
        };
        byte[] key = {
                (byte) 0x00, (byte) 0x01, (byte) 0x20, (byte) 0x01,
                (byte) 0x71, (byte) 0x01, (byte) 0x98, (byte) 0xae,
                (byte) 0xda, (byte) 0x79, (byte) 0x17, (byte) 0x14,
                (byte) 0x60, (byte) 0x15, (byte) 0x35, (byte) 0x94
        };
        word[] plaintext = toWordArr(plain);
        System.out.println("明文：" + wordArrStr(plaintext));
        word[] CipherKey = toWordArr(key);
        System.out.println("密钥：" + wordArrStr(CipherKey));
        word[] cipherText = AES.encrypt(plaintext, CipherKey);
        System.out.println("密文：" + wordArrStr(cipherText));
        word[] newPlainText = AES.decrypt(cipherText, CipherKey);
        System.out.println("明文：" + wordArrStr(newPlainText));





        /*      //简单输出测试
        word[] plaintext = toWordArr(plain);
        System.out.println("明文：" + wordArrStr(plaintext));
        word[] CipherKey = toWordArr(key);
        System.out.println("密钥：" + wordArrStr(CipherKey));
        word[] cipherText = AES.encrypt(plaintext, CipherKey);
        System.out.println("密文：" + wordArrStr(cipherText));
        word[] newPlainText = AES.decrypt(cipherText, CipherKey);
        System.out.println("明文：" + wordArrStr(newPlainText));
        */
    }


    @org.junit.Test //文件加密测试
    public void test01() throws IOException
    {
        //获取使用姓名加密的秘钥
        String[] strings = nameKey();
        //将十六进制字符串转换为byte[]数组
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            key[i] = (byte) ((Character.digit(strings[i].charAt(0),16) << 4) + Character.digit(strings[i].charAt(1),16));
        }

        //从文件中读取明文
        File file = new File("F:\\Java\\AES\\src\\Tony.jpg");
        //输入流
        FileInputStream fis = new FileInputStream(file);
        //输出流
        FileOutputStream fos = new FileOutputStream("F:\\Java\\AES\\src\\Tony1.jpg");
        //字符流
        //FileWriter fw = new FileWriter("F:\\Java\\AES\\src\\Tony1.jpg");
        //BufferedWriter bw = new BufferedWriter(fw);
        //这里使用缓冲流对其进行加速
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        //读取
        byte[] bbuf = new byte[16];
        int read = 0;

        //秘钥处理 -> 无需每次对秘钥进行处理
        word[] CipherKey = toWordArr(key);

        while((read = bis.read(bbuf)) != -1)
        {
            //对读取的明文进行加密
            //明文处理
            word[] plaintext = toWordArr(bbuf);
            //加密
            word[] cipherText = AES.encrypt(plaintext, CipherKey);
            //处理加密之后的文件
            //加密之后的密文为32位字符串，需要转换为byte[]写入文件!!!
            String s = wordArrStr(cipherText);
            //s为32位，每两个字符组成一个十六进制的字符串,即将字符串进行拆分,并转化为16位字节数组     //有效果!!!!!!!!
            String[] strings1 = new String[read];
            String regStr = "\\w\\w";
            Pattern pattern = Pattern.compile(regStr);
            Matcher matcher = pattern.matcher(s);
            int j = 0;
            while(matcher.find())
            {
                String group = matcher.group(0);
                strings1[j++] = group;
            }
            //接下来将String数组转换为byte[]
            byte[] code = new byte[read];
            for (int i = 0; i < read; i++)
            {
                code[i] = (byte) ((Character.digit(strings1[i].charAt(0),16) << 4) + Character.digit(strings1[i].charAt(1),16));
            }

            fos.write(code,0,read);

//            byte[] bytes1 = s.getBytes();
//            byte[] bytes = new byte[16];

//            for (int i = 0; i < s.length(); i++)
//            {
//                bytes[i] = Byte.parseByte(s.substring(i,i+2));
//            }
            //字符流不可行!
            //char[] chars = s.toCharArray();
            //bw.write(chars,0,chars.length);

            //分析
            //bos.write(bytes,0,bytes.length);
            //947c5e1ac785b2f60fb496498ed7b844
        }
    }

    @org.junit.Test //文件解密测试
    public void test02() throws Exception
    {
        //获取使用姓名加密的秘钥
        String[] strings = nameKey();
        //将十六进制字符串转换为byte[]数组
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            key[i] = (byte) ((Character.digit(strings[i].charAt(0),16) << 4) + Character.digit(strings[i].charAt(1),16));
        }

        //解密 -> 这里就省略就一些注释,就是加密的逆过程
        FileInputStream fis = new FileInputStream("F:\\Java\\AES\\src\\Tony1.jpg");
        FileOutputStream fos = new FileOutputStream("F:\\Java\\AES\\src\\Tony2.jpg");

        //FileWriter fw = new FileWriter("F:\\Java\\AES\\src\\Tony2.jpg");
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        //BufferedWriter bw  = new BufferedWriter(fw);
        byte[] bbuf = new byte[16];
        int read = 0;
        word[] CipherKey = toWordArr(key);
        while((read = bis.read(bbuf)) != -1)
        {
            //密文处理
            word[] cipherText = toWordArr(bbuf);
            word[] newPlainText = AES.decrypt(cipherText, CipherKey);
            //同样进行处理
            String s = wordArrStr(newPlainText);
            //存储数组
            String[] strings1 = new String[read];
            byte[] bytes = new byte[read];
            //拆分
            String regStr = "\\w\\w";
            Pattern pattern = Pattern.compile(regStr);
            Matcher matcher = pattern.matcher(s);
            int j = 0;
            while(matcher.find())
            {
                String group = matcher.group(0);
                strings1[j++] = group;
            }
            //String[] -> byte[]
            for (int i = 0; i < read; i++)
            {
                bytes[i] = (byte) ((Character.digit(strings1[i].charAt(0),16) << 4) + Character.digit(strings1[i].charAt(1),16));
            }

            fos.write(bytes,0,read);

//            for (int i = 0; i < s.length(); i++)
//            {
//                bytes[i] = Byte.parseByte(s.substring(i,i+1));
//            }
//            bos.write(bytes,0,bytes.length);
        }

    }

    static word[] toWordArr(byte[] b) {
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

    static String wordArrStr(word[] w) {
        String str = "";
        for (AES_1.word word : w)
            str += word;
        return str;
    }

    static String[] nameKey()
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