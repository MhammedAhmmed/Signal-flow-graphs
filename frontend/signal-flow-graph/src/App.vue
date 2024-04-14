/* eslint-disable */
<template>
    <div class="container">
      <div class="control-window">
        <form @submit="onSubmit">
          <label for="gaininput" style="margin-top: 10px;">gain</label>
          <input type="number"
           placeholder="enter the gain" 
           style="margin: 10px;" 
           id="gaininput" 
           required
           v-model="value"
           name="gain">
          <input type="submit" value="submit" style="float: right; margin-right: 20px;">
          <input type="submit" value="calcen" style="float: right; margin-right: 5px;">
        </form>
        
        
      </div>
      <div class="drawing-window">
        <div class="tool-bar">
          <img src="./assets/pngwing.com.png" @click="(drawingNode) ? drawingNode = false : drawingNode = true" :class="{selected : drawingNode}">
          <img src="./assets/right-arrow.png" @click="source = true; (drawingPath) ? drawingPath = false : drawingPath = true" :class="{selected : drawingPath}">

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
                  width: 50,
                  height: 50,
                  fillAfterStrokeEnabled: true,
                  fontSize: 35,
                }">
            </v-text>
            <!-- <v-circle :config="configCircle"></v-circle> -->
            
          </v-layer>
        </v-stage>
      </div>

    </div>
    
    
</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      value: 0,
      readonly: true,
      drawingNode: false,
      drawingPath: false,
      source: false,
      destination: false,
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
    }
  },
  methods: {
    onSubmit(e) {
      e.preventDefault();
      this.paths[this.paths.length - 1].value = this.value;
      this.value = 0;
    },
    isReadonly() {
      return this.readonly;
    },
    match(x, y) {
      for(var i = 0; i < this.nodes.length; i++) {
        if(x >= this.nodes[i].x - this.nodes[i].radius && x <= this.nodes[i].x + this.nodes[i].radius &&
          y >= this.nodes[i].y - this.nodes[i].radius && y <= this.nodes[i].y + this.nodes[i].radius) {
            return {x: this.nodes[i].x, 
                    y: this.nodes[i].y}
          }
      }
      return null;
    },
    count(x1, y1, x2, y2) {
      let counter = 0;
      for(var i = 0; i < this.paths.length; i++) {
        if((x1 === this.paths[i].points[0] && y1 === this.paths[i].points[1] &&
          x2 === this.paths[i].points[4] && y2 === this.paths[i].points[5]) || 
          (x1 === this.paths[i].points[4] && y1 === this.paths[i].points[5] &&
          x2 === this.paths[i].points[0] && y2 === this.paths[i].points[1])) {
            counter++;
          }
      }
      return counter;
    },
    addNewPath() {
      let counter = this.count(this.startPoint.x, this.startPoint.y, this.endPoint.x, this.endPoint.y);
      console.log(counter)
      counter *= 50;
      let middleX = (this.startPoint.x + this.endPoint.x) / 2;
      let middleY = (this.startPoint.y + this.endPoint.y) / 2;
      let slope = - ((this.endPoint.x - this.startPoint.x) / (this.startPoint.y - this.endPoint.y));
      console.log(slope);
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
      
      console.log(x, y);
      let newPath = {
        points: [Math.round(this.startPoint.x), Math.round(this.startPoint.y),
        Math.round(x), Math.round(y),
        Math.round(this.endPoint.x), Math.round(this.endPoint.y)],
        tension: 1,
        stroke: "black",
        fill: 'black',
        strokeWidth: 4,
        pointerLength: 20,
        pointerWidth: 20,
        value: 0,
      }
      this.paths.push(newPath);
    },


    handleClick(event) {
      const stage = event.target.getStage();
      const pointerPosition = stage.getPointerPosition();
      if(this.drawingNode) {
        let newNode = {
          x: Math.round(pointerPosition.x),
          y: Math.round(pointerPosition.y),
          radius: 10,
          fill: "white",
          stroke: "black",
          strokeWidth: 4,
          draggable: true,
        }
        this.nodes.push(newNode)
      }
      else if(this.drawingPath) {
        if(this.source) {
          const res = this.match(pointerPosition.x, pointerPosition.y);
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
          const res = this.match(pointerPosition.x, pointerPosition.y);
          if(res !== null) {
            this.source = false;
            this.destination = false;
            this.endPoint = res;
            this.readonly = false;
            this.addNewPath();
            return;
          }
          this.drawingPath = false;
          this.destination = false;
          return;
        }
      }
      else
        return;
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
  height: 670px;
}

.container .control-window {
  width: 200px;
  border: 1px solid black;
  border-radius: 5px;
  margin-right: 10px;
}

.container .drawing-window {
  background-color: white;
}

.container .drawing-window .tool-bar {
  width: 500px;
  height: 50px;
  position: relative;
  margin: 0 auto;
  border: 1px solid black;
  border-radius: 5px;
  display: flex;
}

.container .drawing-window .tool-bar img {
  width: 50px;
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
