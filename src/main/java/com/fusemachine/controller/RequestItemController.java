package com.fusemachine.controller;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.User;
import com.fusemachine.entity.UserRequest;
import com.fusemachine.exceptions.NotFoundException;
import com.fusemachine.service.RequestItemService;
import com.fusemachine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TemporalType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RequestItemController {
    @Autowired
    private RequestItemService requestService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(RequestItemController.class);
    private static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) throws Exception {
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormatter, true){
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if(text.equals("today")) {
                    try {
                        setValue(dateFormatter.parse(dateFormatter.format(new Date())));
                    }catch (ParseException ex){
                        logger.error("Parse error: " + ex.getMessage());
                    }
                }
                else {
                    try {
                        super.setAsText(text);
                    }catch (IllegalArgumentException ex){
                        logger.error("Illegal Exception: " + ex.getMessage());
                    }
                }
            }
        };
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }


    @GetMapping("/requests/foods")
    public List<Food> findAllRequestedFoodByDate(@RequestParam(required = false, defaultValue = "today") Date date) throws ParseException {
        return requestService.findAllRequestedFoodByDate(date);
    }

    @GetMapping("/users/{id}/requests")
    public UserRequest findRequestByIdAndDate(@PathVariable int id, @RequestParam(required = false, defaultValue = "today") Date date){
        UserRequest request = requestService.findByUserIdAndDate(id, date);
        if(request == null){
            throw new NotFoundException("User Request not found for today.");
        }
        return request;
    }

    @PostMapping("/users/{id}/requests")
    public void addFoodRequest(@PathVariable int id, @RequestBody UserRequest request){
        User user = userService.findById(id);
        if(user == null) {
            throw new NotFoundException("User with id = " + id + " not found.");
        }
        if(request.getRequestedFoods().isEmpty())
            throw new NotFoundException("Can't request empty list of items.");
        if(requestService.findByUserIdAndDate(id, Calendar.getInstance().getTime()) != null){
            throw new NotFoundException("You have already made a request today.");
        }
        requestService.save(user, request);
    }

    @PutMapping("/users/{id}/requests")
    public void updateFoodRequest(@PathVariable int id, @RequestBody UserRequest request){
        User user = userService.findById(id);
        if(user == null) {
            throw new NotFoundException("User with id = " + id + " not found.");
        }
        if(request.getRequestedFoods().isEmpty()){
            throw new NotFoundException("Can't request empty list of items.");
        }
        UserRequest curRequest = requestService.findByUserIdAndDate(id, Calendar.getInstance().getTime());
        if(curRequest == null)
            throw new NotFoundException("This user haven't made the request today.");

        request.setId(curRequest.getId());
        requestService.save(user, request);
    }

    @DeleteMapping("/users/{id}/requests")
    public void deleteFoodRequest(@PathVariable int id){
        User user = userService.findById(id);
        if(user == null) {
            throw new NotFoundException("User with id = " + id + " not found.");
        }
        UserRequest curRequest = requestService.findByUserIdAndDate(id, Calendar.getInstance().getTime());
        if(curRequest == null)
            throw new NotFoundException("This user haven't made the request today.");

        requestService.deleteById(curRequest.getId());
    }
}
