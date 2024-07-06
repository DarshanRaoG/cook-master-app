<template>
    <div>
        <slot name="foodImg"></slot>
        
        <slot name="foodName"></slot>
        
        <slot name="chefName"></slot>
        
        <slot name="commentCount"></slot>
        
    </div>
</template>

<script>
    export default {
        name: 'RecipeCard'
    }
</script>

<style scoped>

</style>