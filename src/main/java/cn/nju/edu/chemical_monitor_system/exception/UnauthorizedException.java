package cn.nju.edu.chemical_monitor_system.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}