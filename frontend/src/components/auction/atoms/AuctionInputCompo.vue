<template>
<v-row class="rows">
    <v-col cols="12">
        <v-row align="center">
            <v-col cols="auto">
                <div class="label">
                    <p class="font-weight-black">{{label}}</p>
                </div>
            </v-col>
            <v-col>
                <slot></slot>
            </v-col>
        </v-row>
    </v-col>
</v-row>
  
</template>

<script>
export default {
    props: {
        label: {
            //default:"",
            type: String,
        }
    },
}
</script>

<style scoped>
.label {
  width: 200px;
}
.rows {
 width: 1000px;
}
</style>