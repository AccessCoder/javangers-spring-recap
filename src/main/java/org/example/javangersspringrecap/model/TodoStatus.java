package org.example.javangersspringrecap.model;

public enum TodoStatus {

    OPEN("open"),
    IN_PROGRESS("in_progress"),
    DONE("done");

    private final String value;

    TodoStatus(String value) {
        this.value = value;
    }

    public static TodoStatus readValue(String value){
        for (TodoStatus status : values()){
            if (status.value.equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant " + value);
    }
}
