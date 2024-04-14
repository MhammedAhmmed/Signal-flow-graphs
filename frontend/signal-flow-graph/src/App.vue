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
          <input type="submit" value="submit" style="float: right; margin-right: 20px;">
          <button value="calcen" style="float: right; margin-right: 5px;">calcen</button>
        </form>
      </div>
      <div class="drawing-window">
        <div class="tool-bar">
          <img src="./assets/pngwing.com.png"
           @click="(drawingNode) ? drawingNode = false : drawingNode = true" 
           :class="{selected : drawingNode}">

          <img src="./assets/Screenshot_2024-04-14_203519.png" 
          @click="source = true; (drawingPath) ? drawingPath = false : drawingPath = true" 
          :class="{selected : drawingPath}">

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
      value: 1,
      drawingNode: false,
      drawingPath: false,
      source: false,
      destination: false,
      selected: false,
      selectedID: null,
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
      for(var idx = 0; idx < this.paths.length; idx++) {
          if(this.paths[idx].id === this.selectedID) {
            this.paths[idx].stroke = 'black';
            this.paths[idx].value = this.value;
          }
        }
        this.value = 1;
        this.selectedID = null;
    },
    isReadonly() {
      return !this.selected;
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
      // let slope = (y2 - y1) / (x2 - x1);
      for(var i = 0; i < this.paths.length; i++) {
        if((x1 === this.paths[i].points[0] && y1 === this.paths[i].points[1] &&
          x2 === this.paths[i].points[4] && y2 === this.paths[i].points[5]) || 
          (x1 === this.paths[i].points[4] && y1 === this.paths[i].points[5] &&
          x2 === this.paths[i].points[0] && y2 === this.paths[i].points[1])) {
            counter++;
          }
      }
      // for(i = 0; i < this.nodes.length; i++) {
      //   if((x1 === this.nodes[i].x && y1 === this.nodes[i].y) || (x2 === this.nodes[i].x && y2 === this.nodes[i].y))
      //     continue;
      //   if(slope >= Math.abs((y2 - this.nodes[i].y) / (x2 - this.nodes[i].x)) - 0.2 &&
      //      slope <= Math.abs((y2 - this.nodes[i].y) / (x2 - this.nodes[i].x)) + 0.2) {
      //       counter++;
      //   }
      // }
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
      }
      this.paths.push(newPath);
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
  background-color:gainsboro;
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
