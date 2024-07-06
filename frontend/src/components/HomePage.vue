<template>
    <div class="recipe-container">
        <div class="search-bar" v-if="showCards && !favRecipeShow">
            <input type="text" v-model="searchQuery" @input="searchRecipes" placeholder="Search for recipes...">
        </div>

        <RecipeCard v-if="showCards && !favRecipeShow" class="recipe-card" v-for="recipe in cardRecipes" :key="recipe.id" @click="showRecipe(recipe)">

            <template v-slot:foodImg>
                <img :src=recipe.imageUrl alt="Food img" class="food-img">
            </template>

            <template v-slot:foodName>
                <h1 class="food-name">{{ recipe.title }}</h1>
            </template>

            <template v-slot:chefName>
                <p class="chef-name">- {{ getUserById(recipe.userId) }}</p>
            </template>

            <template v-slot:commentCount>
                <p class="comment-count">Comments: {{ recipe.comments ? getCommentCount(recipe.comments) : 0 }}</p>
            </template>

        </RecipeCard>

        <div class="recipe-info-container" v-if="showRecipeInfo">
            <RecipeInfo>
                
                <template v-slot:fav>
                    <div class="favDiv">
                        <button class="fav-button" @click="favUnfav"><img src="../assets/favorite.png" alt="fav-icon" width="30px"></button>
                    </div>
                </template>

                <template v-slot:foodImg >
                    <img :src=recipeObject.imageUrl alt="Food img" class="food-img-2">
                </template>

                <template v-slot:foodName>
                    <h1 class="food-name-2">{{ recipeObject.title }}</h1>
                </template>

                <template v-slot:chefName>
                    <div class="chef-name-2"><span style="font-size: large;margin-right: 10px;">Recipe by</span><img :src="pfp[recipeObject.userId] || defaultPfp" alt="pfp image" class="profile-img smaller-pfp">{{ getUserById(recipeObject.userId) }}</div>
                </template>

                <template v-slot:recipeText>
                    <div class="recipe-text">
                        <h2>Recipe:</h2>
                        <p>{{ recipeObject.recipeText }}</p>
                    </div>
                </template>

                <template v-slot:comments v-if="commentsPresent">
                    <h3>Comments:</h3>

                    <div  v-for="(comment,index) in recipeObject.comments" :key="index" class="recipe-info-comments"> 
                        <h4><img :src="pfp[comment[0]] || defaultPfp" alt="pfp" class="profile-img smaller-pfp"> {{ getUserById(comment[0]) }} </h4>
                        <div class="single-comment-div" >

                            <p v-if="!isEditing(index)" class="single-comment">{{ comment[1] }}</p>
                            <textarea v-else v-model="editCommentText" class="single-comment" cols="25" style="margin-top: 10px;border: 0px;"></textarea>

                            <button v-if="comment[0] == currentUser && !isEditing(index)" @click="startEditing(index, comment[1])" title="Edit"><img src="../assets/pen.png" alt="Edit Icon" width="20px"></button>

                            <div class="action-buttons">
                                <button class="save-comment-button" v-if="comment[0] == currentUser && isEditing(index)" @click="saveEdit(index)" title="Save"><img src="../assets/floppy-disk.png" alt="Save Icon" width="20px"></button>
                                <button v-if="comment[0] === currentUser && isEditing(index)" @click="cancelEdit" title="Cancel"><img src="../assets/cancel.png" alt="Cancel Icon" width="20px"></button>
                                <button class="delete-button" v-if="comment[0] == currentUser && isEditing(index)" @click="deleteComment(index)" title="Delete"><img src="../assets/delete.png" alt="Delete Icon" width="20px"></button>
                            </div>
                        </div>
                    </div>
                </template>

                <template v-slot:addComment>
                    <div class="recipe-add-comment">
                        <img :src="currentUserPfp || defaultPfp" alt="pfp" class="profile-img">
                        <textarea name="addCommentText" placeholder="Give a feedback!" rows="3" v-model="commentText"></textarea>
                        <button @click="sendComment" title="Send comment"><img src="../assets/send-icon.png" alt="Send Icon" width="33px"></button>
                    </div>
                </template>

                <template v-slot:default>
                    <div class="recipe-info-back-button">
                        <button @click="showCardsMethod">Back</button>
                    </div>
                </template>
                
            </RecipeInfo>
        </div>

        <div v-if="favRecipeShow && favRecipes.length === 0" style="font-size: x-large;">Your favorites show up here! 😊</div>

        <RecipeCard v-if="favRecipeShow && !showRecipeInfo" class="recipe-card" v-for="recipe in favRecipes" :key="recipe.id" @click="showRecipe(recipe)">

            <template v-slot:foodImg>
                <img :src=recipe.imageUrl alt="Food img" class="food-img">
            </template>

            <template v-slot:foodName>
                <h1 class="food-name">{{ recipe.title }}</h1>
            </template>

            <template v-slot:chefName>
                <p class="chef-name">- {{ getUserById(recipe.userId) }}</p>
            </template>

            <template v-slot:commentCount>
                <p class="comment-count">Comments: {{ recipe.comments ? getCommentCount(recipe.comments) : 0 }}</p>
            </template>

        </RecipeCard>

    </div>
</template>

<script>
import axios from 'axios';
import RecipeCard from './RecipeCard.vue'
import RecipeInfo from './RecipeInfo.vue'

    export default {
        name: 'HomePage',
        components: {
            RecipeCard,
            RecipeInfo
        },
        props: {
            favRecipeShow: {
                type: Boolean,
                required: false,
            }
        },
        data() {
            return {
                showCards: true,
                showRecipeInfo: false,
                recipeObject: {},
                recipes: [],
                cardRecipes: [],
                users: {},
                commentsPresent: true,
                commentText: '',
                currentUser: -1,
                editingUserCommentIndex: null,
                editCommentText: '',
                ogCommentText: '',
                fav: false,
                favRecipes: [],
                favRecipeIdsList: [],
                pfp: {},
                currentUserPfp: '',
                defaultPfp: 'https://cdn2.vectorstock.com/i/1000x1000/20/61/user-sign-orange-icon-on-black-vector-13392061.jpg',
                searchQuery: ''
            }
        },
        created() {
            this.currentUser = localStorage.getItem('userId');
            
            axios.get(`http://localhost:8081/cookmaster/user?userId=${this.currentUser}`)
                .then(res => {
                    let user = res.data;
                    this.currentUserPfp = res.data.pfpUrl;
                    this.favRecipeIdsList = user.favRecipeIdList;
                    this.favRecipeIdsList.forEach(recipeId => {
                        axios.get(`http://localhost:8081/cookmaster/recipes/getById?recipeId=${recipeId}`)
                        .then(response => {
                            this.favRecipes.push(response.data);
                        })
                    });
                })
                .catch(e => console.log(e));

            this.fetchRecipes();
        },
        computed: {
            computedPfp() {
                return this.ChefPfp || 'https://cdn2.vectorstock.com/i/1000x1000/20/61/user-sign-orange-icon-on-black-vector-13392061.jpg';
            }
        },
        methods: {
            fetchRecipes() {
                axios.get("http://localhost:8081/cookmaster/recipes/home")
                .then(response => {
                    this.cardRecipes = response.data;
                    this.recipes = response.data;
                    this.fetchUsers();
                })
                .catch(e => console.log(e));
            },
            fetchUsers() {
                axios.get("http://localhost:8081/cookmaster/users")
                .then(response => {

                    response.data.forEach(user => {
                        this.pfp[user.id] = user.pfpUrl;
                        this.users[user.id] = user.name;
                    });
                })
                .catch(e => console.log(e));
            },
            getUserById(id) {
                return this.users[id] || "<deleted_user>";
            },
            showRecipe(recipe) {
               
                this.showCards = false;
                this.showRecipeInfo = true;
                this.recipeObject = recipe;

                axios.get(`http://localhost:8081/cookmaster/user?userId=${this.currentUser}`)
                .then(res => {
                    let user = res.data;
                    
                    this.fav = Object.values(user.favRecipeIdList).includes(this.recipeObject.id);
                    if(this.fav)
                        document.querySelector('.fav-button').classList.add('red');
                    else
                        document.querySelector('.fav-button').classList.remove('red');
                })

                if(this.recipeObject.comments == null || Object.keys(this.recipeObject.comments).length === 0)
                    this.commentsPresent = false;
                else
                    this.commentsPresent = true;

                window.scrollTo(0,0);
            },
            showCardsMethod() {
                this.showCards = true;
                this.showRecipeInfo = false;
                this.recipeObject = {};
            },
            getCommentCount(comments) {
                return comments.length;
            },
            sendComment() {
                if(this.commentText === '' || this.commentText == null)
                {
                    alert('Enter a comment!');
                    return;
                }

                axios.put(`http://localhost:8081/cookmaster/recipes/comments/add?recipeId=${this.recipeObject.id}&commenterId=${this.currentUser}`
                ,this.commentText
                ,{
                    headers: {
                        'Content-Type': 'application/JSON'
                    }
                })
                .then(res => {
                    alert(res.data);
                    this.commentsPresent = this.recipeObject.comments.length > 0;
                })
                .catch(e => console.log(e));

                if (this.recipeObject.comments === null)
                    this.recipeObject.comments = [];
                
                this.recipeObject.comments.push([this.currentUser,this.commentText]);
                
                //this.showRecipe(this.recipeObject);    
                
                this.commentText = '';
            },
            isEditing(index) {
                return this.editingUserCommentIndex === index;
            },
            startEditing(index, comment) {
                this.editingUserCommentIndex = index;
                this.editCommentText = comment;
                this.ogCommentText = comment;
            },
            cancelEdit() {
                if(!confirm("Discard changes?"))
                    return;

                this.editingUserCommentIndex = null;
                this.editCommentText = '';
                this.ogCommentText = '';
            },
            saveEdit(index) {
                if(!confirm("Save changes?"))
                    return;

                axios.put(`http://localhost:8081/cookmaster/recipes/comments/edit?recipeId=${this.recipeObject.id}&commenterId=${this.currentUser}`
                ,this.ogCommentText + ';' + this.editCommentText
                ,{
                    headers: {
                        'Content-Type': 'text/plain'
                    }
                });
                
                this.recipeObject.comments[index] = ([this.currentUser,this.editCommentText]);

                this.editingUserCommentIndex = null;
                this.editCommentText = '';
                this.ogCommentText = '';
            },

            deleteComment(index) {
                if(!confirm("Delete comment?"))
                    return;

                axios.delete(`http://localhost:8081/cookmaster/recipes/comments/delete?recipeId=${this.recipeObject.id}&commenterId=${this.currentUser}&commentIndex=${index}`)
                .then(() => {
                    this.recipeObject.comments.splice(index, 1);
                    
                    this.commentsPresent = this.recipeObject.comments.length > 0;

                    this.editingUserCommentIndex = null;
                    this.editCommentText = '';
                    this.ogCommentText = '';
                })
                .catch(e => console.log(e));
            },
            favUnfav() {
                let favButton = document.querySelector('.fav-button');

                if(!this.fav)
                {
                    this.fav = true;
                    favButton.classList.add('red');
                    axios.put(`http://localhost:8081/cookmaster/recipes/fav?userId=${this.currentUser}&recipeId=${this.recipeObject.id}`)
                    .catch(e => console.log(e));
                }
                else
                {
                    this.fav = false;
                    favButton.classList.remove('red');

                    axios.put(`http://localhost:8081/cookmaster/recipes/unfav?userId=${this.currentUser}&recipeId=${this.recipeObject.id}`)
                    .catch(e => console.log(e));
                }

            },
            searchRecipes() {
                
                if (!this.searchQuery) {
                    this.cardRecipes = this.recipes;
                    return this.recipes;
                }

                this.cardRecipes = this.recipes.filter(recipe => {
                    return recipe.keywords.toLowerCase().includes(this.searchQuery.toLowerCase()) || 
                           recipe.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                           recipe.imageUrl.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
                           recipe.recipeText.toLowerCase().includes(this.searchQuery.toLowerCase())       
                    ;
                });
            }
        }
    }
</script>

<style scoped>

.recipe-container {
    margin-top: 85px;
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
}

.recipe-card {
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    width: 300px;
    min-height: 300px;
    transition: transform 0.1s;
    background-color: #2ED668;
    padding-bottom: 10px;
}

.recipe-card:hover {
    transform: scale(1.02);
    cursor: pointer;
}

.profile-img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    box-shadow: 5px 2px 10px black;
    object-fit: cover;
    margin-right: 10px;
    vertical-align: middle;
    transition: 0.1s;
}

.profile-img:hover {
    transform: scale(3);
}

.smaller-pfp {
    width: 40px;
    height: 40px;
}

.search-bar {
    margin: 0px;
    margin-bottom: 10px;
    padding: 0;
    width: 100vw;
    display: flex;
    justify-content: center;
    align-items: center;
}

.search-bar input {
    width: 50%;
    padding: 10px;
    font-size: 1em;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    transition: border-color 0.3s;
}


.fav-button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    padding: 0;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    transition: background-color 0.3s;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: aliceblue;
}

.fav-button img {
    width: 100%;
    height: 100%;
}

.fav-button.red {
    background-color: red;
}

.favDiv {
    display: flex;
    justify-content: flex-end;
    align-content: flex-end;
    width: 100%;
    margin-bottom: 5px;
}


.food-img, .food-img-2 {
    width: 100%;
    height: 200px;
    object-fit: cover;
}

.food-name, .food-name-2 {
    font-size: 1.5em;
    margin: 10px 0;
    text-align: center;
}

.chef-name,.chef-name-2 {
    font-size: 1em;
    color: #070503;
    text-align: center;
    margin: 5px 0;
}

.comment-count {
    font-size: 0.9em;
    color: #070503;
    text-align: center;
    margin: 10px 0;
    font-size: small;
}

.recipe-info-container {
  background-color: #2ED668;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  min-width: 450px;
  max-width: 95%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.food-img-2 {
    width: 400px;
    height: auto;
    min-width: 400px;
    max-height: 500px;
}

.recipe-text {
    max-width: 500px;
    margin: 10px;
    padding: 10px;
}

.recipe-text p{
    text-align: justify; 
    font-size: 1em;
  line-height: 1.6;
  margin-top: 20px;
  word-wrap: break-word;
  white-space: pre-line;
  min-width: 400px;
}

.recipe-info-comments {
  margin-top: 20px;
  padding: 10px;
  background-color: #89F4AE;
  border-radius: 8px;
  width: 450px;
}

.single-comment {
    background-color: white;
    border-radius: 8px;
    padding: 10px;
}

.recipe-info-back-button {
  margin-top: 20px;
  text-align: center;
  align-self: auto;
}

.recipe-info-back-button button {
  padding: 10px 20px;
  font-size: 1em;
  background-color: #3BFC7E;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.recipe-info-back-button button:hover {
  background-color: #00b33f;
}

.recipe-info-back-button button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 255, 81, 0.25);
}

.recipe-info-back-button button:active {
  background-color: #00852c;
}


.recipe-add-comment {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
}

.recipe-add-comment textarea {
  width: 300px;
  max-width: 450px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
  resize: none;
}

.recipe-add-comment button {
  padding: 5px;
  font-size: 1em;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.recipe-add-comment button:hover {
  background-color: #0056b3;
}

.recipe-add-comment button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.recipe-add-comment button:active {
  background-color: #004085;
}

.single-comment-div {
    display: flex;
    flex-direction: column;
}

.single-comment-div textarea {
    margin-bottom: 10px;
    width: 100%;
}

.single-comment-div button {
    align-self: flex-end;
    width: 10%;
    margin-right: 10px;
    padding: 5px;
  font-size: 1em;
  background-color: transparent;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}


.single-comment-div button:hover {
  background-color: #eeb831;
}

.single-comment-div .action-buttons {
    display: flex;
    justify-content: flex-end;
}

.single-comment-div .save-comment-button:hover {
    background-color: #12ba06;
}

.single-comment-div .save-comment-button:focus {
    outline: none;
  box-shadow: 0 0 0 3px rgba(64, 255, 0, 0.25);
}

.single-comment-div .save-comment-button:active {
  background-color: #03b212;
}

.single-comment-div button:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(255, 213, 0, 0.25);
}

.single-comment-div button:active {
  background-color: #b29503;
}

.single-comment-div button.delete-button:hover {
  background-color: #d82638;
}

.single-comment-div button.delete-button:focus {
    outline: none;
  box-shadow: 0 0 0 3px rgba(255, 0, 0, 0.25);
}

.single-comment-div button.delete-button:active {
    background-color: #b20303;
}
</style>
