package servlet.serveurweb;

import jakarta.servlet.http.*;

public class Verification {

    public static boolean est_connecter(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null){
            System.out.println("user de la verif = "+session.getAttribute("user"));
            return true;
        }
        System.out.println("verif: pas d'utilisateur connecter !");

        return false;
    }
}
