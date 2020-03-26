import React,{Component} from "react";
import "../Navbar.css"
import NavTop from "./NavTop";

const backgroundImages = [
    {
        name: "Ayodhya",
        url: "https://images.unsplash.com/photo-1517330357046-3ab5a5dd42a1?ixlib=rb-1.2.1&auto=format&fit=crop&w=1868&q=100"
    },
    {
        name: "Jaipur",
        url: "https://images.unsplash.com/photo-1524230507669-5ff97982bb5e?ixlib=rb-1.2.1&auto=format&fit=crop&w=1868&q=100"
    },
    {
        name: "Amritsar",
        url: "https://images.unsplash.com/photo-1514222134-b57cbb8ce073?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1868&q=100"
    },
    {
        name: "Agra",
        url: "https://images.unsplash.com/photo-1524613032530-449a5d94c285?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1868&q=100"
    },
    {
        name: "Kerala",
        url: "https://images.unsplash.com/photo-1506461883276-594a12b11cf3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1868&q=100"
    }
];


class Navbar extends Component{
    constructor(props){
        super(props);
        this.state = {
            backgroundImageIndex1: 0,
            backgroundImageIndex2: 1,
            counter:0
        }
    }

    componentDidMount(){
        this.updateBackgroundRecursively();
        this.updateCounter();
    }

    updateBackgroundRecursively(){
        setTimeout(()=>{
            this.setState(prevState=>{
                let ind1 = prevState.backgroundImageIndex1+1;
                let ind2 = prevState.backgroundImageIndex2+1;
                if(ind1 == backgroundImages.length)
                    ind1=0;
                if(ind2 == backgroundImages.length)
                    ind2=0;
                return {
                    backgroundImageIndex1: ind1,
                    backgroundImageIndex2: ind2,
                }
            });
            this.updateBackgroundRecursively();
        },20000);
    }

    updateCounter(){
        setTimeout(()=>{
            this.setState(prevState=>{
                return {
                    counter: prevState.counter+1
                }
            });
            this.updateCounter();
        },10000);
    }

    render(){
        let background1 = backgroundImages[this.state.backgroundImageIndex1];
        let background2 = backgroundImages[this.state.backgroundImageIndex2];

        return (
            <div className="navbar">
                <NavTop></NavTop>
                <div className={
                        this.state.counter%2 ? 'background1' : 'background2'
                    }>
                    <h1 className="pseudo-heading1">{background1.name}</h1>
                    <img className="pseudo-background1" src={background1.url}></img>
                </div>
                <div className="realBackground">
                </div>
            </div>
        )
    }
}

export default Navbar;