<template>
    <div class="login-container">
        <h1>Login!</h1>
        <form @submit.prevent="verifyLogin" class="login-form">
            <label for="name">Name:</label>
            <input type="text" name="name" v-model="username" required>

            <label for="password">Password:</label>
            <input type="password" name="password" v-model="password" required>

            <button type="submit">Submit</button>

            <button type="button" @click="$emit('cancel-login')" class="cancel-button">Cancel</button>

        </form>
    </div>
</template>

<script>
import axios from 'axios'

    export default {
        name: "LoginForm",
        data() {
            return {
                username: '',
                password: ''
            }
        },
        methods: {
            verifyLogin() {

                axios.post(`http://localhost:8081/cookmaster/login?name=${this.username}&password=${this.password}`)
                .then(res => {
                    
                    if(res.data === 'User verified!')
                    {
                      axios.get(`http://localhost:8081/cookmaster/user/getId?name=${this.username}`)
                      .then(response => {
                        localStorage.setItem('userId',response.data);
                        alert('Login successful!');
                        this.$emit('login-success');
                      })
                      .catch(err => console.log(err));
                    }
                    else
                      {
                        alert('Invalid credentials!');
                        this.$emit('login-fail');
                      }
                })
                .catch(e => console.log(e));
            }
        }
    }
</script>

<style scoped>


.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  background-color: #2ED668;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.login-container h2 {
  margin-bottom: 20px;
  color: #333;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 300px;
}

.login-form label {
  font-size: 1em;
  color: #484747;
}

.login-form input {
  padding: 10px;
  font-size: 1em;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}

button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

button:active {
  background-color: #004085;
}

.cancel-button {
    background-color: red;
}

.cancel-button:hover {
  background-color: #b30000;
}

.cancel-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 0, 0, 0.25);
}

.cancel-button:active {
  background-color: #850000;
}
</style>