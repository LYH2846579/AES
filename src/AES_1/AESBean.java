package AES_1;

/**
 * @author LYHstart
 * @create 2021-10-08 16:58
 *
 * 针对使用AES加密方式在数据库存储表AES表格的对应类
 */
public class AESBean
{
    private String user;
    private String salt;
    private String password;

    public AESBean() {
    }
    public AESBean(String user, String salt, String password) {
        this.user = user;
        this.salt = salt;
        this.password = password;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AESBean{" +
                "user='" + user + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
