<template>
    <div class="add-recipe-container">
        
        <h1>Share a Recipe!</h1>

        <form @submit.prevent="addNewRecipe" class="add-recipe-form">

            <label for="title">Title:</label>
            <input type="text" name="title" v-model="title" placeholder="e.g. Palak Paneer" required>

            <label for="imageUrl">Image url:</label>
            <input type="text" name="imageUrl" v-model="imageUrl" placeholder="paste image url from the web" required>

            <label for="recipeText">Enter your recipe:</label>
            <textarea name="recipeText" v-model="recipeText" placeholder="Recipe" required></textarea>

            <label for="keywords">Enter some keywords seperated by comma to identify your recipe: (optional)</label>
            <input type="text" name="keywords" v-model="keywords" placeholder="e.g. Palak, Paneer, Gravy">
            

            <button type="submit">Submit</button>

            <button type="button" @click="$emit('cancel-recipe')" class="cancel-button">Clear</button>

        </form>
    </div>
</template>

<script>
import axios from 'axios';

    export default {
        name: 'AddRecipe',
        data() {
            return {
                title: '',
                imageUrl: '',
                recipeText: '',
                keywords: ''
            }
        },
        methods: {
            addNewRecipe() {
                let userId = localStorage.getItem('userId');
                axios.post(`http://localhost:8081/cookmaster/recipes/add?userId=${userId}`,
                {
                    "keywords": this.keywords,
                    "title": this.title,
                    "imageUrl": this.imageUrl,
                    "recipeText": this.recipeText
                },
                {
                    headers: {
                        'Content-Type': 'application/JSON'
                    }
                })
                .then(response => {
                    if(response.data === 'Added Recipe successfully!')
                    {
                        alert('Recipe shared! 😋');
                        this.$emit('add-recipe-success');
                    }
                    else
                    {
                        alert('Could not share recipe!');
                    }
                })
                .catch(e => console.log(e));
            }
        }
    }
</script>

<style scoped>

.add-recipe-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #2ED668;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    max-width: 500px;
    margin-top: 80px;
}

.add-recipe-container h1 {
    margin-bottom: 20px;
    color: #333;
}

.add-recipe-form {
    display: flex;
    flex-direction: column;
    gap: 15px;
    width: 100%;
}

.add-recipe-form label {
    font-size: 1em;
    color: #3f3e3e;
}

.add-recipe-form input,
.add-recipe-form textarea {
    padding: 10px;
    font-size: 1em;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.add-recipe-form textarea {
    resize: vertical;
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