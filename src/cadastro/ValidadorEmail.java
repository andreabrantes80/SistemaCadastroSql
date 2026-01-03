package cadastro;

import java.util.regex.Pattern;

public class ValidadorEmail {


    private static final Pattern PADRAO = Pattern.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    public static boolean ehValido(String email){
        if(email == null) return false;
        String e = email.trim();
        if(e.isEmpty()) return false;
        return PADRAO.matcher(e).matches();
    }
}
