package account.api;

import account.api.security.event.SecurityEventType;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public class SecurityEventResponseEntity<T> extends ResponseEntity<T> {
    private SecurityEventType eventName;
    private String path;
    private Date date;
    private Long subjectAccountId;
    private Long objectAccountId;
    private String subjectAccountName;
    private String objectAccountName;

    //todo use this in everywhere
    public SecurityEventResponseEntity(T body, HttpStatusCode status) {
        super(body, status);
    }

    public SecurityEventType getEventName() {
        return eventName;
    }

    public void setEventName(SecurityEventType eventName) {
        this.eventName = eventName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSubjectAccountId() {
        return subjectAccountId;
    }

    public void setSubjectAccountId(Long subjectAccountId) {
        this.subjectAccountId = subjectAccountId;
    }

    public Long getObjectAccountId() {
        return objectAccountId;
    }

    public void setObjectAccountId(Long objectAccountId) {
        this.objectAccountId = objectAccountId;
    }

    public String getSubjectAccountName() {
        return subjectAccountName;
    }

    public void setSubjectAccountName(String subjectAccountName) {
        this.subjectAccountName = subjectAccountName;
    }

    public String getObjectAccountName() {
        return objectAccountName;
    }

    public void setObjectAccountName(String objectAccountName) {
        this.objectAccountName = objectAccountName;
    }

    public static class Builder<T> {
        private T body;
        private HttpStatusCode status;
        private SecurityEventType eventName;
        private String path;
        private Date date;
        private Long subjectAccountId;
        private Long objectAccountId;
        private String subjectAccountName;
        private String objectAccountName;

        public Builder(T body, HttpStatusCode status) {
            this.body = body;
            this.status = status;
        }

        public Builder<T> eventName(SecurityEventType eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder<T> path(String path) {
            this.path = path;
            return this;
        }

        public Builder<T> date(Date date) {
            this.date = date;
            return this;
        }

        public Builder<T> subjectAccountId(Long subjectAccountId) {
            this.subjectAccountId = subjectAccountId;
            return this;
        }

        public Builder<T> objectAccountId(Long objectAccountId) {
            this.objectAccountId = objectAccountId;
            return this;
        }

        public Builder<T> subjectAccountName(String subjectAccountName) {
            this.subjectAccountName = subjectAccountName;
            return this;
        }

        public Builder<T> objectAccountName(String objectAccountName) {
            this.objectAccountName = objectAccountName;
            return this;
        }

        public SecurityEventResponseEntity<T> build() {
            SecurityEventResponseEntity<T> customResponseEntity = new SecurityEventResponseEntity<>(body, status);
            customResponseEntity.setEventName(eventName);
            customResponseEntity.setPath(path);
            customResponseEntity.setDate(date);
            customResponseEntity.setSubjectAccountId(subjectAccountId);
            customResponseEntity.setObjectAccountId(objectAccountId);
            customResponseEntity.setSubjectAccountName(subjectAccountName);
            customResponseEntity.setObjectAccountName(objectAccountName);
            return customResponseEntity;
        }
    }
}
