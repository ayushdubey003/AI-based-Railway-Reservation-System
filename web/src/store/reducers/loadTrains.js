import { LOAD_SRC_TO_DEST_TRAINS, CHANGE_LOAD_TYPE } from "../actionTypes";


const defaultState = {
    trains: [],
    loading: false
}

export default function loadTrains(state=defaultState,action){
    switch(action.type){
        case LOAD_SRC_TO_DEST_TRAINS:
            return {
                ...state,
                trains: action.trains,
                loading: false
            }
        case CHANGE_LOAD_TYPE:
            return{
                ...state,
                loading: true
            }
        default:
            return state;
    }
}