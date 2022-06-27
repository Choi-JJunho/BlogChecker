package challenge.bolgchecker.service.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class SlackMemberList {

    // You probably want to use a database to store any user information ;)
    static final ConcurrentMap<String, User> usersStore = new ConcurrentHashMap<>();
    private static String token;

    // TODO : @Value 값이 적용되지 않고 null이 나오는 문제
    @Value("${slack.token}")
    public void setToken(String value) {
        SlackMemberList.token = value;
    }

    /**
     * Fetch users using the users.list method
     */
    static void fetchUsers() {
        // you can get this instance via ctx.client() in a Bolt app
        MethodsClient client = Slack.getInstance().methods();
        Logger logger = LoggerFactory.getLogger("my-awesome-slack-app");
        System.out.println(token);
        try {
            // Call the users.list method using the built-in WebClient
            UsersListResponse result = client.usersList(r -> r
                    // The token you used to initialize your app
                    .token(token)
            );
            // invalid_auth Error occur
            saveUsers(result.getMembers());
        } catch (IOException | SlackApiException e) {
            logger.error("error: {}", e.getMessage(), e);
        }
    }

    /**
     * Put users into an object
     */
    static void saveUsers(List<User> users) {
        for (User user : users) {
            // Store the entire user object (you may not need all of the info)
            System.out.println(user);
            // usersStore.put(user.getId(), user);
        }
    }

    public static void main(String[] args) throws Exception {
        fetchUsers();
    }

}