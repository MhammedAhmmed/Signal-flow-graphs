/* eslint-disable */
<template>
    <div class="container">
      <div class="control-window">
        <form @submit="onSubmit">
          <label for="gain" style="margin-top: 10px;"><strong>gain</strong></label>
          <input type="number"
           placeholder="enter the gain" 
           style="margin: 10px;" 
           id="gain" 
           required
           v-model="value"
           name="gain"
           ref="gainInput"
           :readonly="!selected">
          <input type="submit" value="submit" style="float: right; margin-right: 10px;">
        </form>
        <button style="width: 150px; margin: auto; margin-top: 10px;" @click="solve">solve</button>
        <hr>
        <div class="results">
          <strong>Paths</strong>
          <ul>
            <li v-for="item in allPaths" :key="item.id">{{ item }}</li>
          </ul>
          <hr>
          <strong>Individual Loops</strong>
          <ul>
            <li v-for="item in individualCycles" :key="item.id">{{ item }}</li>
          </ul>
          <hr>
          <strong>Δ</strong>
          {{ delta }}
          <hr>
          <strong>Δ's of Paths</strong>
          <ul>
            <li v-for="item in deltasOfPaths" :key="item.id">{{ item }}</li>
          </ul>
          <hr>
          <strong>Transfer Function</strong>
          {{ transferFunction }}
        </div>
      </div>
      <div class="drawing-window">
        <div class="tool-bar">
          <img src="./assets/pngwing.com.png"
           @click="(drawingNode) ? drawingNode = false : drawingNode = true" 
           :class="{selected : drawingNode}">

          <img src="./assets/Screenshot_2024-04-14_203519.png" 
          @click="source = true; (drawingPath) ? drawingPath = false : drawingPath = true" 
          :class="{selected : drawingPath}">

          <button @click="clear" style="float: right;">reset</button>

        </div>
        <v-stage :config="configKonva" style="border: 1px solid black;" @mousedown="handleClick">
          <v-layer>
            <v-arrow
              v-for="item in paths"
              :key = "item.id" 
              :config = "item">
            </v-arrow>

            <v-circle
              v-for="item in nodes"
              :key = "item.id" 
              :config = "item">
              <p>1</p>
            </v-circle>
            <v-text
              v-for="item in paths"
                :key = "item.id" 
                :config = "{
                  text: item.value,
                  fill: 'red',
                  x: item.points[2] - 10,
                  y: item.points[3] - 10,
                  draggable: true,
                  width: 100,
                  height: 50,
                  fillAfterStrokeEnabled: true,
                  fontSize: 35,
                }">
            </v-text>
            <v-text
              v-for="item in nodes"
                :key = "item.id" 
                :config = "{
                  text: (parseInt(item.id) + 1).toString(),
                  fill: 'black',
                  x: item.x - 5,
                  y: item.y - 5,
                  draggable: true,
                  width: 50,
                  height: 50,
                  fillAfterStrokeEnabled: true,
                  fontSize: 10,
                }">
            </v-text>
            <!-- <v-circle :config="configCircle"></v-circle> -->
            
          </v-layer>
        </v-stage>
      </div>

    </div>
    
    
</template>

<script>
import axios from 'axios';


export default {
  name: 'App',
  data() {
    return {
      value: 1,
      drawingNode: false,
      drawingPath: false,
      source: false,
      destination: false,
      selected: false,
      selectedID: null,
      startNode: null,
      endNode: null,
      startPoint: {
        x: null, 
        y: null
      },
      endPoint: {
        x: null, 
        y: null
      },

      configKonva: {
        width: 1300,
        height: 670,
      },
      
      nodes: [],
      paths: [],
      graph: [],
      SourceDestinations: {
        source: null,
        destinations: [],
      },

      allPaths: [],
      individualCycles: [],
      nonTouchingCycles: [],
      deltasOfPaths: [],
      delta: null,
      transferFunction: null,
    }
  },
  methods: {
    onSubmit(e) {
      e.preventDefault();
      let idx = 0;
      let commonPaths = [];
      let indexOfSelected = 0;
      for(var index = 0; index < this.paths.length; index++) {
          if(this.paths[index].id === this.selectedID) {
            this.paths[index].stroke = 'black';
            this.paths[index].value = this.value;
            idx = index;
          }
      }
      for(index = 0; index < this.paths.length; index++) {
        if((this.paths[idx].source == this.paths[index].source) && (this.paths[idx].destination == this.paths[index].destination)) {
          commonPaths.push(this.paths[index].id);
        }
      }

      for(index = 0; index < commonPaths.length; index++) {
        if(commonPaths[index] === this.selectedID)
          indexOfSelected = index;
      }
      for(let i = 0; i < this.graph[this.paths[idx].source].destinations.length; i++) {
        if(this.graph[this.paths[idx].source].destinations[i].destination == this.paths[idx].destination + 1) {
          if(indexOfSelected === 0) {
            this.graph[this.paths[idx].source].destinations[i].weight = this.value;
            break;
          }
            
          else
            indexOfSelected--;
        }
      }
      this.value = 1;
      this.selectedID = null;
    },
    isReadonly() {
      return !this.selected;
    },
    match(x, y, sourceDestination) {
      for(var i = 0; i < this.nodes.length; i++) {
        if(x >= this.nodes[i].x - this.nodes[i].radius && x <= this.nodes[i].x + this.nodes[i].radius &&
          y >= this.nodes[i].y - this.nodes[i].radius && y <= this.nodes[i].y + this.nodes[i].radius) {
            (sourceDestination == 0) ? this.startNode = parseInt(this.nodes[i].id) : this.endNode = parseInt(this.nodes[i].id);
            return {x: this.nodes[i].x, 
                    y: this.nodes[i].y}
          }
      }
      return null;
    },
    count(x1, y1, x2, y2) {
      let counter = 0;
      // let slope = (y2 - y1) / (x2 - x1);
      for(var i = 0; i < this.paths.length; i++) {
        if((x1 === this.paths[i].points[0] && y1 === this.paths[i].points[1] &&
          x2 === this.paths[i].points[4] && y2 === this.paths[i].points[5]) || 
          (x1 === this.paths[i].points[4] && y1 === this.paths[i].points[5] &&
          x2 === this.paths[i].points[0] && y2 === this.paths[i].points[1])) {
            counter++;
          }
      }
      let first = 0;
      let second = 0; 
      for(i = 0; i < this.nodes.length; i++) {
        if(x1 === this.nodes[i].x && y1 === this.nodes[i].y)
          first = parseInt(this.nodes[i].id);
        if(x2 === this.nodes[i].x && y2 === this.nodes[i].y)
          second = parseInt(this.nodes[i].id);
      }
      counter += Math.abs(second - first) - 1;
      return counter;
    },
    addNewPath() {
      let counter = this.count(this.startPoint.x, this.startPoint.y, this.endPoint.x, this.endPoint.y);
      counter *= 50;
      if(this.startPoint.y === this.endPoint.y) {
        this.nodes[this.endNode].y--;
        this.endPoint.y--;
      }
      let middleX = (this.startPoint.x + this.endPoint.x) / 2;
      let middleY = (this.startPoint.y + this.endPoint.y) / 2;
      let slope = - ((this.endPoint.x - this.startPoint.x) / (this.startPoint.y - this.endPoint.y));
      let x = 0
      let y = 0
      if(this.endPoint.x > this.startPoint.x) {
        x = middleX + (counter * Math.sqrt(1 / (1 + Math.pow(slope, 2))));
        y = middleY - slope * (counter * Math.sqrt(1 / (1 + Math.pow(slope, 2))));
      }
      else {
        x = middleX - (counter * Math.sqrt(1 / (1 + Math.pow(slope, 2))));
        y = middleY + slope * (counter * Math.sqrt(1 / (1 + Math.pow(slope, 2))));
      }
      let newPath = {
        id: (this.paths.length).toString(),
        points: [Math.round(this.startPoint.x), Math.round(this.startPoint.y),
        Math.round(x), Math.round(y),
        Math.round(this.endPoint.x), Math.round(this.endPoint.y)],
        tension: 1,
        stroke: "black",
        fill: 'black',
        strokeWidth: 4,
        pointerLength: 20,
        pointerWidth: 20,
        value: 1,
        source: this.startNode,
        destination: this.endNode,
      }
      this.paths.push(newPath);
      this.graph[this.startNode].destinations.push({destination: this.endNode + 1, weight: 1})
    },


    handleClick(event) {
      const stage = event.target.getStage();
      const pointerPosition = stage.getPointerPosition();
      if(this.drawingNode) {
        let newNode = {
          id: (this.nodes.length).toString(),
          x: Math.round(pointerPosition.x),
          y: Math.round(pointerPosition.y),
          radius: 10,
          fill: "white",
          stroke: "black",
          strokeWidth: 4,
        }
        this.nodes.push(newNode)
        this.graph.push({
          source: this.nodes.length,
          destinations: [],
        })
      }
      else if(this.drawingPath) {
        if(this.source) {
          const res = this.match(pointerPosition.x, pointerPosition.y, 0);
          if(res !== null) {
            this.source = false;
            this.destination = true;
            this.startPoint = res;
            return;
          }
          this.source = false;
          return;
        }
        else if(this.destination){
          const res = this.match(pointerPosition.x, pointerPosition.y, 1);
          if(res !== null) {
            this.source = false;
            this.destination = false;
            this.endPoint = res;
            this.readonly = false;
            this.drawingPath = false;
            this.addNewPath();
            return;
          }
          this.drawingPath = false;
          this.destination = false;
          return;
        }
      }
      else if(!this.selected) {
        for(var i = 0; i < this.paths.length; i++) {
          if(pointerPosition.x >= this.paths[i].points[2] - 20 && pointerPosition.x <= this.paths[i].points[2] + 20 &&
          pointerPosition.y >= this.paths[i].points[3] - 20 && pointerPosition.y <= this.paths[i].points[3] + 20) {
            this.paths[i].stroke = 'red';
            this.selectedID = this.paths[i].id;
            this.selected = true;
          }
        }
      }
      else if(this.selected) {
        this.selected = false;
        for(var idx = 0; idx < this.paths.length; idx++) {
          if(this.paths[idx].id === this.selectedID) {
            this.paths[idx].stroke = 'black';
          }
        }
        this.selectedID = null;
      }
      else {
        return;
      }
        
    },
    async solve() {
      await fetch("http://localhost:8080/graph", {
        method: "POST",
        headers: {
          Accept : "application/json",
          "content-type" : "application/json"
        },
        body: JSON.stringify(this.graph),
      }).catch((error) => {
        console.error("Fetch error:", error);
      });

      await axios.get(`http://localhost:8080/graph/paths/${1}/${this.nodes.length}`).then((res => {
        this.allPaths = res.data;
        console.log(this.allPaths);
      })).catch((error) => {
        console.error("Fetch error:", error);
      });

      await axios.get(`http://localhost:8080/graph/individual/cycles`).then((res => {
        this.individualCycles = res.data;
        console.log(this.individualCycles);
      })).catch((error) => {
        console.error("Fetch error:", error);
      });

      await axios.get(`http://localhost:8080/graph/non/touching/cycles`).then((res => {
        this.nonTouchingCycles = res.data;
        console.log(this.nonTouchingCycles);
      })).catch((error) => {
        console.error("Fetch error:", error);
      });

      await axios.get(`http://localhost:8080/graph/deltas`).then((res => {
        this.deltasOfPaths = res.data;
        console.log(this.deltasOfPaths);
      })).catch((error) => {
        console.error("Fetch error:", error);
      });

      await axios.get(`http://localhost:8080/graph/transfer/function`).then((res => {
        this.transferFunction = res.data;
        console.log(this.transferFunction);
      })).catch((error) => {
        console.error("Fetch error:", error);
      });
      this.delta = this.deltasOfPaths.pop();
    },

    clear() {
      this.drawingNode = false
      this.drawingPath = false
      this.source = false
      this.destination = false
      this.selected = false
      this.selectedID = null
      this.startNode = null
      this.endNode = null
      this.nodes = []
      this.paths = []
      this.graph = []
      this.allPaths = []
      this.individualCycles = []
      this.nonTouchingCycles = []
      this.deltasOfPaths = []
      this.delta = null
      this.transferFunction = null
    },
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.container {
  display: flex;
  /* height: 670px; */
  height: auto;
}

.container .control-window {
  width: 200px;
  border: 1px solid black;
  border-radius: 5px;
  margin-right: 10px;
  background-color:gainsboro;
}

.container .drawing-window {
  background-color: white;
}

.container .drawing-window .tool-bar {
  width: 150px;
  height: 50px;
  position: relative;
  margin: 0 auto;
  border: 1px solid black;
  border-radius: 5px;
  display: flex;
  background-color: gainsboro
}

.container .drawing-window .tool-bar img {
  width: 60px;
  height: 50px;
  border-radius: 5px;
}

.container .drawing-window .tool-bar img:hover {
  background-color: gray;
}

.container .drawing-window .tool-bar img.selected {
  background-color: gray;
}
</style>
