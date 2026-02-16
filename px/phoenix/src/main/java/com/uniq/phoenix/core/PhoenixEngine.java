package com.uniq.phoenix.core;

import com.uniq.phoenix.search.WebSearch;

import java.util.Arrays;
import java.util.List;

public class PhoenixEngine {

    private final WebSearch webSearch;

    public PhoenixEngine() {
        this.webSearch = new WebSearch();
    }

    public String process(String inp) {

        if (inp == null || inp.trim().isEmpty()) {
            return "Input cannot be empty.";
        }

        String normalized = inp.trim().toLowerCase();

        if (isExit(normalized)) {
            return exitResponse();
        }
        else if (isHelp(normalized)) {
            return helpResponse();
        }
        else if (isIdentity(normalized)) {
            return identityResponse();
        }
        else if (isSearch(normalized)) {

            String query = extractSearchQuery(normalized);

            if (query.isEmpty()) {
                return "Please provide something to search.\nExample: search java";
            }

            return webSearch.search(query);
        }
        else if (isGreeting(normalized)) {
            return greetingResponse();
        }
        else {
            return fallbackResponse();
        }
    }

    // ---------------- INTENT DETECTION ----------------

    private boolean isSearch(String input) {
        return input.startsWith("search");
    }

    private boolean containsWord(String input, String word) {
        String[] words = input.split("\\s+");
        return Arrays.asList(words).contains(word);
    }

    private boolean isGreeting(String input) {
        return containsWord(input, "hello")
                || containsWord(input, "hi")
                || containsWord(input, "hey");
    }

    private boolean isExit(String input) {
        return containsWord(input, "exit")
                || containsWord(input, "quit")
                || containsWord(input, "stop");
    }

    private boolean isHelp(String input) {
        return containsWord(input, "help")
                || containsWord(input, "command")
                || containsWord(input, "commands")
                || containsWord(input, "guide");
    }

    private boolean isIdentity(String input) {
        return input.contains("who are you")
                || input.contains("what is phoenix")
                || input.contains("introduce yourself");
    }

    // ---------------- RESPONSE METHODS ----------------

    private String greetingResponse() {
        return "Phoenix online. How may I assist you?";
    }

    private String exitResponse() {
        return "Phoenix session terminated.";
    }

    private String extractSearchQuery(String input) {

        if (input.length() <= 6) {
            return "";
        }

        return input.substring(6).trim();
    }

    private String helpResponse() {
        return """
                ---------- Commands Available ----------
                hello, hi, hey
                exit, quit, stop
                help, commands, guide
                who are you
                search <topic>
                """;
    }

    private String identityResponse() {
        return "I am Phoenix, an AI assistant v0. Core system operational.";
    }

    private String fallbackResponse() {
        return "Unknown command. Type 'help' for available commands.";
    }
}

