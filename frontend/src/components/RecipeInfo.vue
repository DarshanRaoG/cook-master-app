<template>
    
        <slot name="fav"></slot>

        <slot name="foodImg"></slot>
        
        <slot name="foodName"></slot>
        
        <slot name="chefName"></slot>
        
        <slot name="recipeText"></slot>

        <slot name="comments"></slot>

        <slot name="addComment"></slot>

        <slot></slot>
    
</template>

<script>
    export default {
        name: 'RecipeInfo',
    }
</script>

<style scoped>

</style>