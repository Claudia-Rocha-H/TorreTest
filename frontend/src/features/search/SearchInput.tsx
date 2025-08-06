import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

/**
 * Search form validation schema
 */
const searchSchema = z.object({
  query: z.string().min(1, 'Search term cannot be empty.'),
});

type SearchFormInputs = z.infer<typeof searchSchema>;

/**
 * Main search input with form validation
 */
interface SearchInputProps {
  onSearch: (query: string) => void;
}

const SearchInput: React.FC<SearchInputProps> = ({ onSearch }) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<SearchFormInputs>({
    resolver: zodResolver(searchSchema),
  });

  const onSubmit = (data: SearchFormInputs) => {
    onSearch(data.query);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex justify-center mt-10">
      <div className="relative w-3/5 max-w-2xl">
        <FontAwesomeIcon
          icon={faSearch}
          className="absolute left-5 top-1/2 -translate-y-1/2 text-input-placeholder transition-colors duration-300 text-base"
        />
        <input
          type="text"
          placeholder="Search people or organizations by name"
          {...register('query')}
          autoComplete="off"
          spellCheck={false}
          className={`w-full py-4 pl-12 pr-4 bg-input-bg border rounded-full text-light-text text-base outline-none transition-colors duration-300 placeholder:text-input-placeholder
            ${errors.query ? 'border-badge focus:border-badge' : 'border-border focus:border-primary'}
          `}
        />
        {errors.query && (
          <p className="text-badge text-sm mt-1 ml-12">{errors.query.message}</p>
        )}
      </div>
    </form>
  );
};

export default SearchInput;