package org.jasig.cas.authentication.principal;


public class ChangePasswordCredentials implements Credentials {

	private static final long serialVersionUID = 8801383728234816416L;

    private String orgpwd;

    private String pwd1;
    
    private String pwd2;
    
    public String getOrgpwd() {
		return orgpwd;
	}

	public void setOrgpwd(String orgpwd) {
		this.orgpwd = orgpwd;
	}

	public String getPwd1() {
		return pwd1;
	}

	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}

	public String getPwd2() {
		return pwd2;
	}

	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}

	public String toString() {
        return "[orgpwd: " + this.orgpwd + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangePasswordCredentials that = (ChangePasswordCredentials) o;

        if (orgpwd != null ? !orgpwd.equals(that.orgpwd) : that.orgpwd != null) return false;
        if (pwd1 != null ? !pwd1.equals(that.pwd1) : that.pwd1 != null) return false;
        if (pwd2 != null ? !pwd2.equals(that.pwd2) : that.pwd2 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orgpwd != null ? orgpwd.hashCode() : 0;
        result = 31 * result + (pwd1 != null ? pwd1.hashCode() : 0);
        result = 31 * result + (pwd2 != null ? pwd2.hashCode() : 0);
        return result;
    }
}