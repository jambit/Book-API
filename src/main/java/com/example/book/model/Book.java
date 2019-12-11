package com.example.book.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Pattern;

@ApiModel(description = "Details about the book")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Book {

    @Id
    @ApiModelProperty(notes = "The unique _id, which is used to get the Book in the Database")
    private String _id;

    @ApiModelProperty(notes = "The title of the book")
    private String title;

    @ApiModelProperty(notes = "The genre of the book")
    private String genre;

    @ApiModelProperty(notes = "The author of the book")
    private String author;

    @ApiModelProperty(notes = "The date of the book's publication")
    @Pattern(regexp = "^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}$", message = "Publication Date is not correct formatted (dd.mm.yyyy)")
    private String publicationDate;

    @ApiModelProperty(notes = "The total number of pages")
    private String pageNumber;
}
