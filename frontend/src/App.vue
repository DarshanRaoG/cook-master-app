<template>
  <div>
    <NavBar v-if="showNavBar"
            @navToHome="showHome"
            @userProfile="showProfile"
            @addRecipe="getRecipeMethod"
            @favRecipes="showFavRecipes"
            @logout="logoutMethod"
    />

    <component :is="compoName" 
               @login="loginMethod"
               @logout="logoutMethod"
               @cancel-login="showWelcome" 
               @signup="signupMethod" 
               @cancel-signup="showWelcome" 
               @login-success="showHome"
               @login-fail="loginMethod"
               @signup-success="loginMethod"
               @add-recipe-success="showHome"
               @cancel-recipe="getRecipeMethod"

               @edit-recipe="getEditRecipeMethod"
               :recipe="recipeObj"
               @edit-recipe-success="showProfile"
               @cancel-edit-recipe="showProfile"

               @delete-recipe-success="showProfile"
               
               :favRecipeShow="favRecipeShow"

               :key="componentKey"
    />
    
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import WelcomePage from './components/WelcomePage.vue'
import LoginForm from './components/LoginForm.vue'
import SignUp from './components/SignUp.vue'
import HomePage from './components/HomePage.vue'
import AddRecipe from './components/AddRecipe.vue'
import UserProfile from './components/UserProfile.vue'
import EditRecipe from './components/EditRecipe.vue'

  export default {
    name: 'App',
    components: {
      NavBar,
      WelcomePage,
      LoginForm,
      SignUp,
      HomePage,
      AddRecipe,
      UserProfile,
      EditRecipe
    },
    data() {
      return {
        showNavBar: false,
        compoName: "WelcomePage",
        componentKey: 0,
        recipeObj: {},
        favRecipeShow: false
      }
    },
    mounted() {
      if(localStorage.getItem('userId'))
        this.showHome();
      else
        this.showWelcome();
    },
    methods: {
      showHome() {
        this.favRecipeShow = false;
        this.compoName = "HomePage";
        this.showNavBar = true;
        this.componentKey += 1;
      },
      showWelcome() {
        this.compoName = "WelcomePage";
        this.showNavBar = false;
      },
      loginMethod() {
        this.compoName = "LoginForm";
        this.showNavBar = false;
        localStorage.setItem('userId', '');
      },
      signupMethod() {
        this.compoName = "SignUp";
        this.showNavBar = false;
      },
      logoutMethod() {
        this.compoName = "WelcomePage";
        this.showNavBar = false;
        localStorage.removeItem('userId');
      },
      getRecipeMethod() {
        this.compoName = "AddRecipe";
        this.componentKey += 1;
      },
      showProfile() {
        this.compoName = "UserProfile";
        this.componentKey += 1;
      },
      getEditRecipeMethod(recipe) {
        this.recipeObj = recipe;
        this.compoName = "EditRecipe";
        this.componentKey += 1;
      },
      showFavRecipes() {
        this.favRecipeShow = true;
        this.compoName = "HomePage";
        this.componentKey += 1;
      }

    }
    }
</script>

<style scoped>
.fillSpace {
  margin: 150px;
}

</style>