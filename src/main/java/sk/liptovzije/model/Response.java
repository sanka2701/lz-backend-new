package sk.liptovzije.model;

public class Response<T> {
    private T data;
    private String jwt;

    public Response(T data, String jwt) {
        this.data = data;
        this.jwt = jwt;
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
}
