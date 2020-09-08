package com.fusemachine.service;

import com.fusemachine.entity.Food;
import com.fusemachine.entity.User;
import com.fusemachine.entity.UserRequest;
import com.fusemachine.exceptions.ResourcesNotFoundException;
import com.fusemachine.repo.FoodRepository;
import com.fusemachine.repo.RequestItemRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RequestItemService {

    private static final Logger logger = LoggerFactory.getLogger(RequestItemService.class);

    @Autowired
    private RequestItemRepository requestRepo;

    @Autowired
    private FoodRepository foodRepo;

    public UserRequest findByUserIdAndDate(int id, Date date) {
        return requestRepo.findByUserIdAndDateEquals(id, date);
    }

    public UserRequest findById(int id){
        return requestRepo.findById(id).orElse(null);
    }

    @Transactional
    public void save(User user, UserRequest request) {
        request.setUser(user);
        request.setDate(Calendar.getInstance().getTime());
        for(Food food : request.getRequestedFoods()){
            if(!foodRepo.findById(food.getId()).isPresent()){
                throw new ResourcesNotFoundException("Food with id = " + food.getId() + " not found.");
            }
        }
        requestRepo.save(request);
    }

    @Transactional
    public void deleteById(int id) {
        requestRepo.deleteById(id);
    }

    @Transactional
    public JSONArray findAllRequestedFoodByDate(Date date){
        JSONArray jsonArray = new JSONArray();
        for (Integer[] requestItem : requestRepo.findAllRequestedFoodByDateEquals(date)){
            Food food = foodRepo.findById(requestItem[0]).get();
            int count = requestItem[1];
            JSONObject object = new JSONObject();
            object.put("food", food);
            object.put("requestCount", count);
            jsonArray.add(object);

        }
        return jsonArray;
    }
}
