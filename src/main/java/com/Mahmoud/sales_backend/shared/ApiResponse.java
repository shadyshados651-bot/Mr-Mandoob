package com.Mahmoud.sales_backend.shared;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private boolean success;
    private String message;
    private Object data;

    public static ApiResponse error(String msg) {
        return new ApiResponse(false, msg, null);
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(true, "success", data);
    }

}