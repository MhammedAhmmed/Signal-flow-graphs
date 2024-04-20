<template>
  <div class="home">
       <h1>Welcome to Control Systems' Stability Tester</h1>
       <h3>based on Routh-Hurwitz method</h3> 
     <div class="degree">
      <label class="label">Input the degree of your characteristics equation</label>
      <input type="number" v-model="degree" class="numeric" :disabled="show">
      <p class="label">your choice is {{degree}}</p>
     </div>  
     <button class="confirmation" @click="check_and_proceed()" :disabled="show">confirm</button>
     <p class="error" v-if="error">Please re-check your equation's degree</p>
  </div>
  <p v-if="show" class="label">Enter the coefficients of your equation:</p>
  <div style="display:flex; justify-content:center;" v-if="show" class="equation">
       <div v-for="(power, index) in equation_powers" :key="power">
             <input class="cofficients" type="number" v-model="equation_cofficients[index]">
             S<sup>{{power}}</sup>
            <span v-if="power>0">+</span>
            <span v-else> = 0</span>
       </div>
  </div>
  <p v-show="show">{{equation_cofficients}}</p>
  <button class="confirmation" v-show="show" @click="test_system()">Test system</button>
   <state :rhs="rhs" :condition="condition"  v-if="showState" @close="remove()" />
</template>

<script>

import state from '../components/state.vue';
export default {
  components: { state },
  name: 'HomeView',
  data(){
    return{
       degree: null,
       show: false,
       error: false,
       equation_powers: [], 
       equation_cofficients: [],
       cofficient:0,
       showState:false, 
       condition:null,
       stabilityResult:null,
       rhs:[]
     }
  },
  methods:{
    check_and_proceed() {
      if (this.degree > 0 && this.degree <= 100) {
        this.error = false;
        this.show = true;
        for (let i = this.degree; i >= 0; i--) {
          this.equation_powers.push(i);
          this.equation_cofficients.push(0);
        }
      } else {
        this.error = true;
      }
    },
    handleCoefficientInput(index, event) {
      const coefficient = parseFloat(event.target.value);
      this.equation_cofficients[index] = isNaN(coefficient) ? 0 : coefficient;
    },
 async test_system() {
  try {
    const response = await fetch(`http://localhost:8080/isStable?coefficients=${this. equation_cofficients}`);
    const data = await response.json();
    console.log(data); 

    if (data === 1) {
      this.condition = true;
    } else if (data === 0) {
      this.condition = false;
    } else {
      console.error('Invalid stability response:', data);
      this.stabilityResult = 'Error: Invalid response from server.';
    }
  } catch (error) {
    console.error(error);
  }
   if(this.condition===false)
   {
      this.showroots();
   }

  this.showState = true;
},
    remove()
    {
      this.showState=false;
      location.reload();
    },
async showroots() {
  try {
    const response = await fetch(`http://localhost:8080/showRoots?coefficients=${this. equation_cofficients}`);
    const data = await response.json();
    this.rhs=data;
  } catch (error) {
    console.error(error);
  }
}

  }
}
</script>


<style>
h3
{
   color: blueviolet;
}
.numeric
{
  display: block;
   margin-left: 45%;
   margin-right:45% ;
   margin-top: 10px;
   background-color:whitesmoke;
   border-radius:5px ;
   height: 25px;
   width: 10%;
   
}
.numeric:hover
{
   background-color: rgba(245, 245, 245, 0.5)
}
.label
{
   font-size: 1.3em;
}
.error
{
  color: red;
  font-size: 1.5em;
}
.confirmation
{
   color:black;
   font-size: 1.25em;
   height: 50px;
   border-radius:25px ;
   border:solid;
   width: 150px;
   background-color: yellow;
}
.cofficients
{
   width:45px;
   background-color: beige;
}
</style>
