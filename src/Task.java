import java.time.LocalDateTime;

public class Task {
    private Integer id;
    private String description;
    private boolean status;
    private LocalDateTime createdAt;

    public Task(Integer id, String description, boolean status, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return this.createdAt;
    }

    public void setTime(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
