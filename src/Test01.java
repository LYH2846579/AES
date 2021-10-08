import org.junit.Test;

import javax.sound.midi.Soundbank;

/**
 * @author LYHstart
 * @create 2021-10-07 20:15
 */
public class Test01
{
    @Test   //名字转换函数 -> 将姓名转换为
    public void test1()
    {
        String s = "网安刘亚东";
        byte[] bytes = s.getBytes();
        String name = "";
        String substr = "";
        for(byte b:bytes)
        {                                   //左闭右开区间!!!
            substr = Integer.toBinaryString(b).substring(24,32);
            name += substr;
        }
        //最后补齐8位数
        name += "10011100";
        System.out.println(name);
    }

    @Test   //转换为16进制
    public void test2()
    {
        String s = "网安刘亚东";
        byte[] bytes = s.getBytes();
        String name = "";
        String substr = "";
        for(byte b:bytes)
        {
            substr = Integer.toHexString(b).substring(6,8) + " ";
            name += substr;
        }
        //补齐两位数
        name += "17";
        System.out.println(name);
    }

    @Test
    public void test3()
    {
        String[] strings = nameKey();
        //将十六进制字符串转换为byte[]数组
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++)
        {
            key[i] = (byte) ((Character.digit(strings[i].charAt(0),16) << 4) + Character.digit(strings[i].charAt(1),16));
        }
        for(byte b:key)
            System.out.println(b);
    }

    public String[] nameKey()
    {
        //就是为了排除重复代码
        int age = 0;

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
