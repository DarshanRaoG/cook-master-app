<template>
    <div class="navbar">
        <button id="home-button" @click="$emit('navToHome')" title="Home">
          <img src="../assets/home-2.png" alt="home">
        </button>
        
        <button id="profile-button" @click="$emit('userProfile')" title="Profile">
          <img src="../assets/profile.png" alt="profile">
        </button>
        
        <button id="add-recipe-button" @click="$emit('addRecipe')" title="Add Recipe">
          <img src="../assets/plus.png" alt="add recipe">
        </button>
        
        <button id="fav-recipe-button" @click="$emit('favRecipes')" title="Favorites">
          <img src="../assets/favorite.png" alt="fav">
        </button>
        
        <button id="logout-button" @click="$emit('logout')" title="Log out">
          <img src="../assets/logout.png" alt="logout">
        </button>
    </div>
</template>

<script>
    export default {
        name: 'NavBar',
    }
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 2px;
  left: 20%;
  width: 60vw;
  border-radius: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 10px;
  background-color: rgba(38, 37, 37, 0.925);
  padding: 10px;
  z-index: 1000;
}

.navbar img {
  width: 30px;
  margin: 0;
  padding: 0;
}

.navbar button {
  background-color: #2ED668;
  border: none;
  border-radius: 4px;
  padding: 7px;
  width: 20%;
  max-width: 100px;
  font-size: 1em;
  cursor: pointer;
  transition: background-color 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

.navbar button:hover {
  background-color: #00b33f;
}

.navbar button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 255, 81, 0.25);
}

.navbar button:active {
  background-color: #00852c;
}


.navbar #logout-button{
  background-color: red;
}

.navbar #logout-button:hover {
  background-color: #b30000;
}

.navbar #logout-button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 0, 0, 0.25);
}

.navbar #logout-button:active {
  background-color: #850000;
}

@media screen and (max-width: 600px) {
  .navbar {
    left: 19%;
  }
}

</style>