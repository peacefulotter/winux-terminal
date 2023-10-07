import { FiBookmark, FiAirplay, FiCodepen, FiThumbsUp, FiTrendingUp, FiCodesandbox, FiBold, FiItalic, FiCode, FiUnderline, FiLink, FiList, FiImage, FiAnchor } from 'react-icons/fi'

import ConnectionStatus from "./ConnectionStatus";
import LumaBtn from "./LumaBtn";
import Welcome from "./Welcome";

export default function Header() {
    return (
        <div className='flex gap-8'>
          <div className='flex flex-col gap-2'>
            <Welcome />
            <ConnectionStatus />
          </div>
          <div className='flex flex-wrap gap-1'>
            <LumaBtn theme='blue' icon={FiBookmark}>Bookmarks</LumaBtn>
            <LumaBtn theme='red' icon={FiAirplay}>Imagine</LumaBtn>
            <LumaBtn theme='yellow' icon={FiCodepen}>A new world</LumaBtn>
            <LumaBtn theme='cyan' icon={FiThumbsUp}>Its crazy</LumaBtn>
            <LumaBtn theme='green' icon={FiTrendingUp}>Trending</LumaBtn>
            <LumaBtn theme='purple' icon={FiCodesandbox}>Codesandbox</LumaBtn>
            
            <LumaBtn theme='pink' icon={FiAnchor}></LumaBtn>
            <LumaBtn theme='purple' icon={FiList}></LumaBtn>
            <LumaBtn theme='blue' icon={FiBold}></LumaBtn>
            <LumaBtn theme='cyan' icon={FiCode}></LumaBtn>
            <LumaBtn theme='green' icon={FiLink}></LumaBtn>
            <LumaBtn theme='yellow' icon={FiUnderline}></LumaBtn>
            <LumaBtn theme='orange' icon={FiImage}></LumaBtn>
            <LumaBtn theme='red' icon={FiItalic}></LumaBtn>
          </div>
        </div>
    )
}