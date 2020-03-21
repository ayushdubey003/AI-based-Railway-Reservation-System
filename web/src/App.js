import React,{Component} from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component{

  constructor(props){
    super(props);
  }

  componentDidMount(){
    fetch("http://localhost:5000/list")
      .then(res => res.json())
      .then(res => console.log(res));
  }

  render(){
    return(<div>
      Main Page
    </div>)
  }
}

export default App;
