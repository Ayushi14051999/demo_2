package com.example.demo.controller;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.example.demo.entity.User;
import com.example.demo.controller.UserController;
import com.example.demo.services.UserServices;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userServices;

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        // Add some mock user data to the list

        when(userServices.getUserList()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(users.size()));

        verify(userServices, times(1)).getUserList();
    }

    @Test
    public void testGetUserById() throws Exception {
        int userId = 1;
        User mockUser = new User(); // Create a mock User object
        mockUser.setId(userId);

        when(userServices.getUserById(userId)).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/{id}", userId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId));

        verify(userServices, times(1)).getUserById(userId);
    }

    @Test
    public void testAddUser() throws Exception {
        // Create a mock user
        User newUser = new User();
        newUser.setFname("John");
        newUser.setLname("Doe");
        newUser.setAge(30);

        // Mock the service's createUser method
        Mockito.when(userServices.createUser(Mockito.any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/addUser")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"fname\": \"John\", \"lname\": \"Doe\", \"age\": 30}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(newUser.getId()));
    }

    @Test
    public void testAddUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        // Add some mock user data to the list

        when(userServices.createUserList(userList)).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/addUsers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("[]")) // You should provide a valid JSON array of users here
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(userList.size()));

        verify(userServices, times(1)).createUserList(userList);
    }



    @Test
    public void testUpdateUser() throws Exception {
        // Create a mock user
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setFname("UpdatedFirstName");
        updatedUser.setLname("UpdatedLastName");
        updatedUser.setAge(35);

        // Mock the service's updateUserById method
        Mockito.when(userServices.updateUserById(Mockito.any(User.class))).thenReturn(updatedUser);
        
        // Mock the repository's findById method
        Mockito.when(userServices.getUserById(Mockito.anyInt())).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/updateUsers/")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\": 1, \"fname\": \"UpdatedFirstName\", \"lname\": \"UpdatedLastName\", \"age\": 35}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedUser.getId()));
    }
}
