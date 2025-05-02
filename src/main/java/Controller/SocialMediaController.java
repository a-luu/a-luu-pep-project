package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/accounts", this::getAllAccountsHandler);
        app.get("/messages/{message_id}", this::getMessageFromIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.post("/register", this::postAccountHandler);
        app.get("/accounts/{account_id}/messages",
                this::getAllMessagesFromUserHandler);
        app.post("/login", this::loginHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    */

    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    private void getAllAccountsHandler(Context ctx) {
        List<Account> accounts = accountService.getAllAccounts();
        ctx.json(accounts);
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        List<Account> accounts = accountService.getAllAccounts();
        boolean match = false;
        for (Account a : accounts) {
            if (a.getAccount_id() == message.getPosted_by()) {
                match = true;
                Message addedMessage = messageService.addMessage(message);
                if(addedMessage!=null){
                    ctx.json(mapper.writeValueAsString(addedMessage));
                }else{
                    ctx.status(400);
                }
                break;
            }
        }
        if (match == false)
            ctx.status(400);
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        List<Account> accounts = accountService.getAllAccounts();
        boolean match = false;
        Message original = messageService.getMessageFromId(message_id);
        if (original != null) {
            for (Account a : accounts) {
                if (a.getAccount_id() == original.getPosted_by()) {
                    match = true;
                    Message updatedMessage = messageService.updateMessage(message_id, message);
                    System.out.println(updatedMessage);
                    if(updatedMessage == null){
                        ctx.status(400);
                    }else{
                        ctx.json(mapper.writeValueAsString(updatedMessage));
                    }
                    break;
                }
            }
        }        
        if (match == false)
            ctx.status(400);
    }
    
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);
        if(message == null){
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(message));
        }

    }

    public void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageFromIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageFromId(message_id);
        if(message == null){
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    private void getAllMessagesFromUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int user_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesFromUser(user_id);
        if(messages == null){
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(messages));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account updatedAccount = accountService.login(account);
        if(updatedAccount == null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(updatedAccount));
        }

    }

}