package Sample;

/**
 * Created by Амфетамин on 26.10.14.
 */
public class AuthData {
    public AuthData(int hash, String email) {
        this.hash = hash;
        this.email = email;
    }

    private int hash;
    private String email;

    public int getHash() {
        return hash;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthData that = (AuthData) o;

        if (!email.equals(that.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
