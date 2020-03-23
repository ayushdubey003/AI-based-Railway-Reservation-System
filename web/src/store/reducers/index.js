import {combineReducers} from "redux"; 
import initialAppData from "./initialAppData";

const rootReducer = combineReducers({
    initialAppData,
});

export default rootReducer;