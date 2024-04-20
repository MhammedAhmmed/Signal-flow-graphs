<template>
    
     <div class="Gstate" v-if="condition" @click.self="closemodel()">
          <span class="answer">
            NICE : Your system is stable
          </span>        
     </div>
     <div class="Bstate" v-else @click="closemodel()">
         <span class="answer">
           unfortunately: Your system is unstable
          </span>
     </div>

       <div class="roots" v-if="rhs && rhs.length > 0&& appear">
      <span>number of Right-hand side roots is: {{rhs.length}} </span>
      <hr>
      <ul>
        <li v-for="root in rhs" :key="root">
          {{ (root[0]).toFixed(3) }} + {{ (root[1]).toFixed(3) }}j
        </li>
      </ul>
    </div>

</template>

<script>
export default {
    props:['condition','rhs'],
    data(){
        return{
            state:null,
            appear:true,
        }
    },
    methods:
    {
        closemodel()
        {
             this.appear=false;
            this.$emit('close');
        }

    }

}
</script>

<style>
.Gstate, .Bstate {
  margin-top: 20px;
  height: 200px;
  width: 40%;
  text-align: center;
  margin-left: 30%;
  margin-right: 30%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.Gstate {
  background-color: aquamarine;
}

.Bstate {
  background-color: crimson;
}

.answer {
  text-align: center;
  color: black;
  font-size: 1.5em;
}
.roots
{
   height:200px;
   background-color: azure;
   border: dotted;
   margin-top:15px;
   color: #000;
   width: 30%;
   margin-left: 35%;
   margin-right: 35%;

}
</style>