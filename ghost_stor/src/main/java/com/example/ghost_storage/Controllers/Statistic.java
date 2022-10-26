package com.example.ghost_storage.Controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Statistic {
    @Value("${server.ip}")
    private String host;

    @Value("${server.dashboard_port}")
    private String port;

    @GetMapping("/counts")
    public String activate(Model model)
    {
        var url = String.format("http://%s:%s", host, port);
        model.addAttribute("url", url);
        return "statistic";
    }
}
