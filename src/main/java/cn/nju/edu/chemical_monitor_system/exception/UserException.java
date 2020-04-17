package cn.nju.edu.chemical_monitor_system.exception;


public class UserException extends RuntimeException {
    public UserException(String msg) {
        super(msg);
    }

    public UserException() {
        super();
    }
}
