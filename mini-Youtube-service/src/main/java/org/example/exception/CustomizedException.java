package org.example.exception;


public class CustomizedException extends RuntimeException{

    // fixed serialVersionUID is used to avoid serialization and deserialization failure if this class
    // is modified and the input json or object file is still the old one
    // (else every modification will lead to a new one
    private static final long serialVersionUID = 1L;

    private final String code;

    public CustomizedException(String msg){
        super(msg);
        code = "500";
    }

    public CustomizedException(String msg, String code){
        super(msg);
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
