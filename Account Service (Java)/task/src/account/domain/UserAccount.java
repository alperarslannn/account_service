package account.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
public class UserAccount {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "USER_DETAILS_SEQ", allocationSize = 1)
    private Long id;
    @Column
    private String name;
    @Column
    private String lastname;
    @Column(name = "email_username", unique = true)
    private String email;
    @Column
    private String password;
    @Column
    private String salt;
    @OneToMany(mappedBy = "userAccount")
    private List<Employee> employees;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "useraccounts_groups",
            joinColumns =@JoinColumn(name = "useraccount_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    private List<Group> roles = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Group> getRoles() {
        return roles;
    }

    public void setRoles(List<Group> roles) {
        this.roles = roles;
    }
    public void addRole(Group group) {
        roles.add(group);
    }

    public void removeRole(Group group) {
        roles.remove(group);
    }

    public List<String> getRolesAsString() {
        return roles.stream().map(role -> role.getName().toString()).toList();
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority)new SimpleGrantedAuthority(role.getName())).toList();
    }
}
