const fs = require('fs');

let input = fs.readFileSync(process.argv[2],'utf8');

let [properties, ...data] = input.split('\n');
let [nv,ne,nr,nc,cacheSize] = properties.split(' ');

//console.log(nv,ne,nr,nc,cacheSize);

let videos = data.splice(0,1)[0].split(' ').map((size,i)=>({id:i,size}));


//console.log('Videos: '+videos.length);

let endpoints = [];
for(let i=0;i<ne;i++){
	let [dcLatency,numCaches] = data.splice(0,1)[0].split(' ');
	let connections = [];
	for(let j=0;j<numCaches;j++){
		let [cacheId,cacheLatency] = data.splice(0,1)[0].split(' ');
		connections.push({cacheId,cacheLatency});
	}
	endpoints.push({id:i,dcLatency,connections});
}

let requests = [];
for(let i=0;i<nr;i++){
	let [id,endpointId,numOfReq] = data.splice(0,1)[0].split(' ');
	requests.push({id,endpointId,numOfReq});
}

//console.log('End Points: '+endpoints.length);
//console.log('Requests: '+requests.length);

let assignments = [];
videos.forEach(v=>{
	let assignment = assignments.find(a=>v.size<a.spaceLeft);
	if(!assignment && assignments.length<nc){
		assignment = {
			spaceLeft: cacheSize,
			videos: []
		};
		assignments.push(assignment);
	}
	if(assignment){
		assignment.videos.push(v.id);
		assignment.spaceLeft -= v.size;
	}
});

console.log(assignments.length);
assignments.forEach((a,i)=>console.log(`${i} ${a.videos.join(' ')}`));
