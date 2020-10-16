package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.entity.Customer;
import onlineShop.service.CustomerService;

@Controller
public class RegistrationController {

    // step1: autoware在service里的component
    @Autowired
    private CustomerService customerService;

    // value: 通过page里的 navbar.jsp, 找到点击SignUp后出发的http请求 value = "/customer/registration"
    @RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
    public ModelAndView getRegistrationForm() { // 执行register业务逻辑时，需要返回新的页面
        Customer customer = new Customer();
        // 找到 register.jsp，去生成是用户点击register后所显示的页面
        // 返回到dispatchServlet，由它去找到对应的jsp，并根据其内容生产html的content返回到浏览器
        return new ModelAndView("register", "customer", customer);

        // 因为之创建新的user，所以返回的是空的 Customer()，将来可以通过get使用户能够修改user info
        // return new ModelAndView("register", "customer", new Customer());
    }


    @RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
    public ModelAndView registerCustomer(@ModelAttribute Customer customer,
                                         BindingResult result) {
        // bindingResult相当于validation的过程，再绑定的过程中查看是否有异常

        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }
        // 若无异常，就写入DB
        customerService.addCustomer(customer);
        modelAndView.setViewName("login");
        modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
        // 最后跳转到登录页面
        return modelAndView;
    }
}
