package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

       public MessageService(){
        messageDAO = new MessageDAO();
    }
    
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageFromId(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message addMessage(Message message) {
        if (message.getMessage_text() == "" || message.getMessage_text().length() > 255)
            return null;
        else
            return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessagesFromUser(int user_id) {
        return messageDAO.getAllMessagesFromUser(user_id);
    }

    public Message updateMessage(int message_id, Message message){
        if (messageDAO.getMessageById(message_id) == null || message.getMessage_text() == "" || message.getMessage_text().length() > 255)
            return null;
        else {
            messageDAO.updateMessage(message_id, message);
            return messageDAO.getMessageById(message_id);
        }
    }

    public Message deleteMessage(int message_id){
        Message message = messageDAO.getMessageById(message_id);
        messageDAO.deleteMessage(message_id);
        if (messageDAO.getMessageById(message_id) == null)
            return message;
        else
            return null;
    }
}
