package sk.liptovzije.model;

public class Response<T> {
    private T data;
    private String jwt;
    private String message;

    public Response() {

    }

    public Response(T data, String jwt) {
        this.data = data;
        this.jwt = jwt;
        this.message = null;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
