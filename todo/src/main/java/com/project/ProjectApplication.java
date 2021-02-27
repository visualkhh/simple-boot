package com.project;

import com.simple.boot.Wow;

public class ProjectApplication {
    public static void main(String[] args) {
        Wow wow = new Wow();
        System.out.println("----projects name: "+wow.name());
    }
}
