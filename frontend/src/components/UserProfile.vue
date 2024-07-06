<template>
    <div class="user-profile-container">
        <div class="delete-profile-button">
            <h1 style="margin: 0px;">Profile</h1>
            <button class="delete-button" @click="deleteProfile" title="Delete"><img src="../assets/delete.png" alt="Delete Icon" width="20px"></button>
        </div>
        <div class="user-info-box">
            <div class="action-buttons">
                <button v-if="!isEditing" @click="startEdit" title="Edit"><img src="../assets/pen.png" alt="Edit Icon" width="20px"></button>
                <button class="save-button" v-if="isEditing" @click="saveEdit" title="Save"><img src="../assets/floppy-disk.png" alt="Save Icon" width="20px"></button>
                <button v-if="isEditing" @click="closeEdit(true)" title="Cancel"><img src="../assets/cancel.png" alt="Cancel Icon" width="20px"></button>
            </div>

            <label for="pfp">Profile Picture:</label>
            <textarea name="pfp" id="pfp" v-model="pfp"></textarea>

            <div class="previewPfpDiv">Preview: <img :src="computedPfp" alt="???" class="profile-img"></div>
            
            <label for="name">Name:</label>
            <textarea name="name" id="username" v-model="name"></textarea>
    
            <label for="password">Password:</label>
            <textarea name="password" id="pwd" v-model="password"></textarea>
        </div>

        <div class="recipes-shared-box">
            

            <label for="recipes">Recipes shared:</label>
            <div v-if="recipes.length === 0">
                <h1>Zero 😲</h1>
            </div>
            <div name="recipes" class="single-recipe-div" v-for="recipe in recipes" :key="recipe.id">
                
                <div class="action-buttons">
                    <button @click="startEditRecipe(recipe)" title="Edit"><img src="../assets/pen.png" alt="Edit Icon" width="20px"></button>
                    <button class="delete-button" @click="deleteRecipe(recipe)" title="Delete"><img src="../assets/delete.png" alt="Delete Icon" width="20px"></button>
                </div>
                
                <img :src="recipe.imageUrl" class="recipe-image" alt="recipe image">

                <div class="recipe-info">
                    <h3 id="recipes">{{ recipe.title }}</h3>
                    
                    <p id="kwds">{{ (recipe.keywords)? recipe.keywords.split(',').map(keyword => '#'+keyword.trim()).join(', ') : '#keywords' }}</p>
                   
                    <p>Comments: {{ recipe.comments ? recipe.comments.length : 0 }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

    export default {
        name: 'UserProfile',
        data() {
            return {
                ogPfp: '',
                ogName: '',
                ogPwd: '',
                pfp: '',
                name: '',
                password: '',
                recipeIds: [],
                recipes: [],
                isEditing: false,
                userId: -1
            }
        },
        computed: {
            computedPfp() {
                return this.pfp || 'https://cdn2.vectorstock.com/i/1000x1000/20/61/user-sign-orange-icon-on-black-vector-13392061.jpg';
            }
        },
        created() {
            
            this.userId = localStorage.getItem('userId');

            axios.get(`http://localhost:8081/cookmaster/user?userId=${localStorage.getItem('userId')}`)
            .then(res => {
                let user = res.data;
                this.ogPfp = user.pfpUrl;
                this.pfp = user.pfpUrl;
                this.ogName = user.name;
                this.name = user.name;
                this.ogPwd = user.password;
                this.password = user.password;
                this.recipeIds = user.recipeIdList;

                this.getRecipeById(this.recipeIds);

                document.getElementById('pfp').readOnly = true;
                document.getElementById('username').readOnly = true;
                document.getElementById('pwd').readOnly = true;
            })
            .catch(e => console.log(e));
        },
        methods: {
            deleteProfile() {
                if(!confirm('Are you sure? This action will delete the user profile but your recipes and comments won\'t be affected!'))
                    return;

                let userId = localStorage.getItem('userId');
                axios.delete(`http://localhost:8081/cookmaster/delete?userId=${userId}`)
                .then(res => {
                    alert(res.data);
                    this.$emit('logout');
                }).catch(e => console.log(e));
            },
            getRecipeById(recipeIds) {
                this.recipeIds.forEach(recipeId => {
                    axios.get(`http://localhost:8081/cookmaster/recipes/getById?recipeId=${recipeId}`)
                    .then(response => {
                        this.recipes.push(response.data)
                    });
                });
            },
            startEdit() {
                document.getElementById('pfp').readOnly = false;
                document.getElementById('username').readOnly = false;
                document.getElementById('pwd').readOnly = false;
                this.isEditing = true;
            },
            saveEdit() {
                if(this.name === '' || this.name === null || this.password === null || this.password === '') 
                {
                    alert('Name or password can NOT empty!');
                    return;
                }
                if(!confirm("Save changes?"))
                    return;

                let newPfp = (this.ogPfp != this.pfp)? this.pfp : null;
                let newName = (this.ogName != this.name)? this.name : null;
                let newPwd = (this.ogPwd != this.password)? this.password : null;

                let url = `http://localhost:8081/cookmaster/editUser?userId=${this.userId}`;

                if (newPfp !== null || newName !== null || newPwd !== null) {
                    url += '&';
                    if (newPfp !== null) url += `pfpUrl=${newPfp}&`;
                    if (newName !== null) url += `name=${newName}&`;
                    if (newPwd !== null) url += `password=${newPwd}&`;
                    url = url.replace(/&$/, '');
                }

                axios.put(url)
                .then(res => {

                    alert(res.data);
                    if (newName !== null) this.ogName = newName;
                    if (newPwd !== null) this.ogPwd = newPwd;
                    if (newPfp !== null) this.ogPfp = newPfp;

                    this.closeEdit(false);
                })
                .catch(e => console.log(e));
                
            },
            closeEdit(askConfirmation) {
                if(askConfirmation && !confirm("Discard changes?"))
                    return;
                this.name = this.ogName;
                this.password = this.ogPwd;
                this.pfp = this.ogPfp;

                document.getElementById('pfp').readOnly = true;
                document.getElementById('username').readOnly = true;
                document.getElementById('pwd').readOnly = true;
                this.isEditing = false;
            },
            startEditRecipe(recipe) {
                this.$emit('edit-recipe',recipe);
            },
            deleteRecipe(recipe) {
                if(!confirm(`Note: ${recipe.title} recipe will be deleted! Go ahead?`))
                    return;

                axios.delete(`http://localhost:8081/cookmaster/recipes/delete?recipeId=${recipe.id}`)
                .then(res => {
                    alert(res.data);
                    this.$emit('delete-recipe-success');
                })
                .catch(e => console.log(e));
            },
            getCommentCount(comments) {
                let totalLength = 0;

                for (const key in comments) {
                    if (comments.hasOwnProperty(key)) {
                        totalLength += Object.entries(comments[key]).length;
                    }
                }
                return totalLength;
            }
        }
    }
</script>

<style scoped>

.user-profile-container {
    display: flex;
    flex-direction: column;
    background-color: #2ED668;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    max-width: 600px;
    margin-top: 80px;
    gap: 40px;
}

.profile-img {
    width: 70px;
    height: 70px;
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

.user-info-box ,.recipes-shared-box {
    background-color: #89F4AE;
    padding:20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    gap: 10px;
}

.previewPfpDiv {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
}


.recipes-shared-box {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.recipes-shared-box label {
    align-self: flex-start;
}

label {
    font-size: 1.5em;
    color: #3f3e3e;
    text-align: left;
}

textarea {
    width: 100%;
    padding: 10px;
    font-size: 1.2em;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-top: 10px;
    margin-bottom: 15px;
    resize: none;
}

.single-recipe-div {
    display: flex;
    flex-direction: column;
    align-items: center;
    background-color: #ffffff;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-top: 10px;
    margin-bottom: 15px;
    width: 90%;
    gap: 10px;
}

.single-recipe-div .action-buttons, .delete-profile-button {
    justify-self: flex-end;
    align-self: flex-end;
}

.single-recipe-div .delete-button:hover, .delete-profile-button .delete-button:hover {
  background-color: #d82638;
}

.single-recipe-div .delete-button:focus, .delete-profile-button .delete-button:focus {
    outline: none;
  box-shadow: 0 0 0 3px rgba(255, 0, 0, 0.25);
}

.single-recipe-div .delete-button:active, .delete-profile-button .delete-button:active{
    background-color: #b20303;
}

.recipe-image {
    width: 200px;
    border-radius: 4px;
    margin-right: 15px;
}

.recipe-info {
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin: 5px;
    padding: 5px;
}

.recipe-info h3 {
    margin: 0;
    color: #333;
}

.recipe-info p {
    margin: 5px 0;
    color: #555;
}

#kwds {
    color: blue;
}

.action-buttons, .delete-profile-button {
    display: flex;
    gap: 15px;
    align-items: center;
    justify-content: flex-end;
}

.delete-profile-button {
    width: 100%;
    justify-content: space-between;
}

.action-buttons button, .delete-profile-button button{
    padding: 5px;
    background-color: transparent;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.delete-profile-button {
    margin: 0px;
    padding: 0;
}

.action-buttons button img {
    width: 20px;
}

.action-buttons button:hover {
    background-color: #eeb831;
}

.action-buttons button:focus {
    outline: none;
    box-shadow: 0 0 0 3px rgba(255, 213, 0, 0.25);
}

.action-buttons button:active {
    background-color: #b29503;
}


.action-buttons .save-button:hover {
    background-color: green;
}

.action-buttons .save-button:focus {
    outline: none;
  box-shadow: 0 0 0 3px rgba(64, 255, 0, 0.25);
}

.action-buttons .save-button:active {
  background-color: #03b212;
}



</style>