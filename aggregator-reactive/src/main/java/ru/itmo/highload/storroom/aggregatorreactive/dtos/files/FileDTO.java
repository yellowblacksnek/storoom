package ru.itmo.highload.storroom.aggregatorreactive.dtos.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {

    private String username;
    private String filename;
}