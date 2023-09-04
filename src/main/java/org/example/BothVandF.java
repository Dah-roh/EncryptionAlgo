package org.example;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class BothVandF {

    private Long id;
    private String token; //findByToken(String token)
    private Boolean isValid = true;

    private Long userId;

    private Calendar expirationTime = dateExpiration();

    private Calendar dateExpiration() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 15);
        return cal;
    }

    private boolean isExpired(Calendar expirationTime){
        Calendar cal = Calendar.getInstance();
        return cal.before(expirationTime);
    }



    //endpoint for forget or verify <p> CLICK LINK <a href= 127.0.0.1:1233/verify/forgetPassword.getToken()/></P>

    //HttpServletResponse and HttServletRequest
    //@PathVariable or @
}
