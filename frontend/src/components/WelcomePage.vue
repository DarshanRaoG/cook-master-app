<template>
    <div class="container">
        <div class="chef-img">
            <img src="../assets/chef.png" alt="">
        </div>

        <div class="welcome-text">
            <h1>Welcome to Cook Master!</h1>
            <h3>Learn recipes, share your own, and much more!</h3>
        
            <button class="login-button" @click="$emit('login')">Login</button>
            <button class="signup-button" @click="$emit('signup')">Sign up</button>
        </div>
    </div>
</template>

<script>

    export default {
        name: "WelcomePage",
        
    }
</script>

<style scoped>

.container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: center;
    background-color: #2ED668; /* Light background */
    margin: 10px;
    margin-top: 50px;
    text-align: center;
    padding: 20px;
    border-radius: 8px; /* Rounded corners */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Subtle shadow */
}

.chef-img img {
    margin: 20px;
    width: 300px; /* Adjust as needed */
    height: auto; /* Adjust as needed */
}

.welcome-text {
    padding: 10px;
}

button {
    padding: 10px 20px; /* Add horizontal padding */
    margin: 5px;
    transition: transform 0.1s, box-shadow 0.1s; /* Transition transform and box-shadow */
    border: none;
    border-radius: 8px; /* Rounded corners */
    background-color: #007bff; /* Primary color */
    color: white; /* Text color */
    cursor: pointer; /* Pointer cursor */
    font-size: 1em; /* Font size */
}

button:hover {
    transform: scale(1.05); /* Scale on hover */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Add shadow on hover */
}

.login-button:hover {
  background-color: #0056b3;
}

.login-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.login-button:active {
  background-color: #004085;
}

.login-button {
    background-color: #007bff; /* Primary color for login button */
}

.signup-button:hover {
  background-color: #91da1d;
}

.signup-button {
    background-color: greenyellow;
    color: black;
}

.signup-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 255, 72, 0.25);
}

.signup-button:active {
  background-color: #008521;
}
</style>