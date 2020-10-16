
package onlineShop.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity //每个class都需要有这个annotation
@Table(name = "authorities")  // 如果不用class做table name的话
public class Authorities implements Serializable {

    // 随意写
    private static final long serialVersionUID = 8734140534986494039L;

    @Id // PK
    private String emailId;

    private String authorities; // one to many

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
