import React from "react";

export default function searchDropdownItem(props){
    return (
        <div className='dropdown-item'>
            <div className="station-code">{props.children[0]}</div>
            <div className="station-name">{props.children[1]}</div>
        </div>
    )
}