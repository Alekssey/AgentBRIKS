package ru.mpei.brics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
    private String paramName;
    private List<String> values = new ArrayList<>();
    private List<String> dateTime = new ArrayList<>();
}
