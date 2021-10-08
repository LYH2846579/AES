package AES_1;
/**
 * @author LYHstart
 * @create 2021-10-08 10:11
 */

public class word {
    public byte[] word;

    public word(byte[] b) {
        word = new byte[4];
        for (int i = 0; i < 4; i++)
            word[i] = b[i];
    }

    public word(word w) {
        word = new byte[4];
        for (int i = 0; i < 4; i++)
            word[i] = w.word[i];
    }

    //获取word
    public byte[] getWord() {
        return word;
    }

    @Override   //重写Object.toString()
    public String toString() {
        String str = "";
        for (byte b : word)
            str += Integer.toHexString((b & 0xff) + 0x100).substring(1);
        return str;
    }

    // 在GF(2^8)上的多项式加法
    static word add(word a, word b) {
        word c = new word(new byte[4]);
        for (int i = 0; i < 4; i++)
            c.word[i] = add(a.word[i], b.word[i]);
        return c;
    }

    //在GF(2^8)上的多项式乘法
    static word multiply(word a, word b) {
        word c = new word(new byte[4]);
        c.word[0] = add(add(add(multiply(a.word[0], b.word[0]), multiply(a.word[3], b.word[1])),multiply(a.word[2], b.word[2])),multiply(a.word[1], b.word[3]));
        c.word[1] = add(add(add(multiply(a.word[1], b.word[0]), multiply(a.word[0], b.word[1])),multiply(a.word[3], b.word[2])),multiply(a.word[2], b.word[3]));
        c.word[2] = add(add(add(multiply(a.word[2], b.word[0]), multiply(a.word[1], b.word[1])),multiply(a.word[0], b.word[2])),multiply(a.word[3], b.word[3]));
        c.word[3] = add(add(add(multiply(a.word[3], b.word[0]), multiply(a.word[2], b.word[1])),multiply(a.word[1], b.word[2])),multiply(a.word[0], b.word[3]));
        return c;
    }

    //在GF(2^8)上的多项式倍乘
    static word xtime(word a) {
        word b = new word(new byte[4]);
        for (int i = 0; i < 4; i++)
            b.word[i] = a.word[(i + 1) % 4];
        return b;
    }
    /***************************************************************************************************/
    static int m = 0x11b;   //m=100011011

    //在GF(2^8)上的加法
    static byte add(byte a, byte b) {
        return (byte) (a ^ b);
    }

    //在GF(2^8)上的求模
    static byte mod(int a, int b) {
        String str_a = Integer.toBinaryString(a);
        String str_b = Integer.toBinaryString(b);
        if (str_a.length() < str_b.length())
            return (byte) a;
        return mod(a ^ (b << (str_a.length() - str_b.length())), b);
    }

    //在GF(2^8)上的乘法
    static byte multiply(byte a, byte b) {
        int op = a & 0xff;
        char[] c = Integer.toBinaryString((b & 0xff) + 0x100).substring(1).toCharArray();
        int r = 0;
        for (int i = 0; i < c.length; i++)
            if (c[i] == '1')
                r ^= op << (7 - i);
        return mod(r, m);
    }

    //在GF(2^8)上的乘法逆
    static byte inverse(byte a) {
        if (a == 0) return 0;
        byte b = -128;
        while (mod(multiply(a, b), m) != 1)
            b++;
        return b;
    }

    //在GF(2^8)上的倍乘
    static byte xtime(byte a) {
        int r = (a & 0xff) << 1;
        if (r > 127)
            return mod(r, m);
        return (byte) r;
    }
}


