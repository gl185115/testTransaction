/**
 * Draw polygons or stroke lines on canvas
 */

var res = res || {};

(function(){
	
	res.canvas = {

		polygon: function(canvas, edges, colorStart, colorStop){
			var context = canvas.getContext('2d');
			context.beginPath();
			var grad  = context.createLinearGradient(colorStart[0], colorStart[1], colorStop[0], colorStop[1]);
			grad.addColorStop(0, colorStart[2]);
			grad.addColorStop(1, colorStop[2]);
			context.fillStyle = grad;
			context.moveTo(edges[0][0], edges[0][1]); 
			for (var i = 1; i < edges.length; i++){
				context.lineTo(edges[i][0], edges[i][1]); 
				context.lineTo(edges[i][0], edges[i][1]); 				
			}
			context.closePath(); 
			context.fill(); 
			return canvas;
		},
		stroke: function(canvas, positions, lineWidth, color){
			var context = canvas.getContext('2d');
			context.beginPath();
			context.strokeStyle = color;
			context.lineWidth = lineWidth;
			context.moveTo(positions[0][0], positions[0][1]);
			for (var i = 1; i < positions.length; i++){
				context.lineTo(positions[i][0], positions[i][1]);
			}
			context.stroke();
			return canvas;
		}

	};
	
})();

